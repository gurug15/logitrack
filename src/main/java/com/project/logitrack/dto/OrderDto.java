package com.project.logitrack.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderDto {
	private Long id;
    private String customername;
    private String status;
    private BigDecimal totalprice;
}
