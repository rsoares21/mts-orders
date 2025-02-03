package com.mouts.model;


import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int idProduto;
    private BigDecimal valor;
}

