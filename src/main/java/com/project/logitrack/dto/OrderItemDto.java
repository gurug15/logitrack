package com.project.logitrack.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.PrePersist;
import lombok.Data;

@Data
public class OrderItemDto {
	private Long id;
    private String productName;  // assuming OrderItem has such a field
    private Integer quantity;
    private BigDecimal price;
    
    private Double totalPrice;
    
}
