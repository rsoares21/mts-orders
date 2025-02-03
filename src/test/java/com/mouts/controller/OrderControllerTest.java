package com.mouts.controller;

import com.mouts.exception.OrderNotFoundException;
import com.mouts.model.Order;
import com.mouts.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarOrders() {
        Order order = new Order();
        when(orderService.listarOrders()).thenReturn(Arrays.asList(order));

        List<Order> orders = orderController.listarOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderService, times(1)).listarOrders();
    }

    @Test
    void testListarOrdersRecebido() {
        Order order = new Order();
        when(orderService.listarOrdersPorStatus("RECEBIDO")).thenReturn(Arrays.asList(order));

        List<Order> orders = orderController.listarOrdersRecebido();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderService, times(1)).listarOrdersPorStatus("RECEBIDO");
    }

    @Test
    void testCriarOrder() {
        Order order = new Order();
        when(orderService.salvarOrder(order)).thenReturn(order);

        Order createdOrder = orderController.criarOrder(order);

        assertNotNull(createdOrder);
        verify(orderService, times(1)).salvarOrder(order);
    }

    @Test
    void testEntregarOrders() {
        Order order = new Order();
        when(orderService.atualizarStatusOrders(anyList(), eq("ENTREGUE"))).thenReturn(Arrays.asList(order));

        ResponseEntity<String> response = orderController.entregarOrders(Arrays.asList(order));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Sucesso: Pedidos atualizados para o status ENTREGUE.", response.getBody());
        verify(orderService, times(1)).atualizarStatusOrders(anyList(), eq("ENTREGUE"));
    }

    @Test
    void testEntregarOrdersNotFound() {
        Order order = new Order();
        doThrow(new OrderNotFoundException("Order não encontrado")).when(orderService).atualizarStatusOrders(anyList(), eq("ENTREGUE"));

        ResponseEntity<String> response = orderController.entregarOrders(Arrays.asList(order));

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Erro: Order não encontrado", response.getBody());
        verify(orderService, times(1)).atualizarStatusOrders(anyList(), eq("ENTREGUE"));
    }
}
