package com.mouts.model;


import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistory {

    private Date dataAtualizacao;
    private String status;
    
}

