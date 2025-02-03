package com.mouts.model;


import lombok.*;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products") // Define a coleção no MongoDB
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int idProduto;
    private BigDecimal valor;
}

