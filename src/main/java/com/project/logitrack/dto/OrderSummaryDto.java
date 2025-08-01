package com.project.logitrack.dto;

import java.math.BigDecimal;

import com.project.logitrack.Entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDto {
	private Long id;
    private String customername;
    private String status;
    private BigDecimal totalprice;
    
}
