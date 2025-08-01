package com.project.logitrack.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemFormDto {
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
