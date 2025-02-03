package com.mouts.controller;

import com.mouts.exception.OrderNotFoundException;
import com.mouts.model.Order;
import com.mouts.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> listarOrders() {
        return orderService.listarOrders();
    }

    @GetMapping("/recebidas")
    public List<Order> listarOrdersRecebido() {
        return orderService.listarOrdersPorStatus("RECEBIDO");
    }

    @PostMapping
    public Order criarOrder(@RequestBody Order order) {
        return orderService.salvarOrder(order);
    }

    @PutMapping("/entregar")
    public ResponseEntity<String> entregarOrders(@RequestBody List<Order> orders) {
        try {
            orderService.atualizarStatusOrders(orders, "ENTREGUE");
            return ResponseEntity.ok("Sucesso: Pedidos atualizados para o status ENTREGUE.");
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(404).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: Ocorreu um erro ao atualizar os pedidos.");
        }
    }
}
