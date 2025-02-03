package com.mouts.model;


import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistory implements Serializable {

    private Date dataAtualizacao;
    private String status;
    
}

