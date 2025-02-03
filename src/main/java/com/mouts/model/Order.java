package com.mouts.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "orders") // Define a coleção no MongoDB
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id; // MongoDB usa String como ID por padrão

    private String codigoOrder;
    private BigDecimal valorTotal;
    private Date dataCriacao;
    private String status;
    private List<Product> produtosList = new ArrayList<>();
    private List<OrderHistory> history = new ArrayList<>();

    public void calcularValorTotal() {
        this.valorTotal = produtosList.stream()
                               .map(Product::getValor)
                               .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
