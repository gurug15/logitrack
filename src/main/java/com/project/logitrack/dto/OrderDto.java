package com.project.logitrack.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class OrderDto {
	private Long id;
    private String customername;
    private String status;
    private String city;
    private LocalDate orderDate;
    private BigDecimal totalprice;
}
