package com.mouts.service;

import com.mouts.exception.OrderDuplicadoException;
import com.mouts.exception.OrderNotFoundException;
import com.mouts.model.Order;
import com.mouts.model.Product;
import com.mouts.repository.OrderRepository;
import com.mouts.constants.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarOrders() {
        Order order = new Order();
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        List<Order> orders = orderService.listarOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testSalvarOrder() {
        Order order = new Order();
        order.setCodigoOrder("123ABC");
        order.setProdutosList(Arrays.asList(new Product(123, BigDecimal.valueOf(100))));
        when(orderRepository.findByCodigoOrder(order.getCodigoOrder())).thenReturn(Optional.empty());
        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = orderService.salvarOrder(order);

        assertNotNull(savedOrder);
        assertEquals(OrderStatus.RECEBIDO, savedOrder.getStatus());
        verify(orderRepository, times(1)).findByCodigoOrder(order.getCodigoOrder());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testSalvarOrderDuplicado() {
        Order order = new Order();
        order.setCodigoOrder("123ABC");
        when(orderRepository.findByCodigoOrder(order.getCodigoOrder())).thenReturn(Optional.of(order));

        assertThrows(OrderDuplicadoException.class, () -> orderService.salvarOrder(order));
        verify(orderRepository, times(1)).findByCodigoOrder(order.getCodigoOrder());
        verify(orderRepository, never()).save(order);
    }

    @Test
    void testListarOrdersPorStatus() {
        Order order = new Order();
        when(orderRepository.findByStatus(OrderStatus.RECEBIDO)).thenReturn(Arrays.asList(order));

        List<Order> orders = orderService.listarOrdersPorStatus(OrderStatus.RECEBIDO);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findByStatus(OrderStatus.RECEBIDO);
    }

    @Test
    void testAtualizarStatusOrders() {
        Order order = new Order();
        order.setCodigoOrder("123ABC");
        when(orderRepository.findByCodigoOrder(order.getCodigoOrder())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        List<Order> orders = orderService.atualizarStatusOrders(Arrays.asList(order), OrderStatus.ENTREGUE);

        assertNotNull(orders);
        assertEquals(OrderStatus.ENTREGUE, orders.get(0).getStatus());
        verify(orderRepository, times(1)).findByCodigoOrder(order.getCodigoOrder());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testAtualizarStatusOrdersNotFound() {
        Order order = new Order();
        order.setCodigoOrder("123ABC");
        when(orderRepository.findByCodigoOrder(order.getCodigoOrder())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.atualizarStatusOrders(Arrays.asList(order), OrderStatus.ENTREGUE));
        verify(orderRepository, times(1)).findByCodigoOrder(order.getCodigoOrder());
        verify(orderRepository, never()).save(order);
    }
}
