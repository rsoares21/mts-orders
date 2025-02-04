package com.mouts.service;

import com.mouts.exception.OrderDuplicadoException;
import com.mouts.exception.OrderNotFoundException;
import com.mouts.model.Order;
import com.mouts.model.OrderHistory;
import com.mouts.repository.OrderRepository;
import com.mouts.constants.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> listarOrders() {
        return orderRepository.findAll();
    }

    public Order salvarOrder(Order order) {
        Optional<Order> orderExistente = orderRepository.findByCodigoOrder(order.getCodigoOrder());
        if (orderExistente.isPresent()) {
            throw new OrderDuplicadoException("Order duplicado com o código: " + order.getCodigoOrder());
        }
        order.setDataCriacao(new Date());
        order.setStatus(OrderStatus.RECEBIDO);
        order.calcularValorTotal();
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setDataAtualizacao(order.getDataCriacao());
        orderHistory.setStatus(order.getStatus());
        order.getHistory().add(orderHistory);
        return orderRepository.save(order);
    }

    @Cacheable(value = "ordersCache", key = "#status")
    public List<Order> listarOrdersPorStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @CacheEvict(value = "ordersCache", allEntries = true)
    public List<Order> atualizarStatusOrders(List<Order> orders, String status) {
        for (Order order : orders) {
            Optional<Order> orderExistente = orderRepository.findByCodigoOrder(order.getCodigoOrder());
            if (orderExistente.isPresent()) {
                orderExistente.get().setStatus(status);
                OrderHistory orderHistory = new OrderHistory(new Date(), orderExistente.get().getStatus());
                orderExistente.get().getHistory().add(orderHistory);
                orderRepository.save(orderExistente.get());
            } else {
                throw new OrderNotFoundException("Order não encontrado com o código: " + order.getCodigoOrder());
            }
        }
        return orders;
    }
}
