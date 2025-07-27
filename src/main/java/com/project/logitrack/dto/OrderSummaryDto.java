package com.project.logitrack.dto;

import java.math.BigDecimal;

import com.project.logitrack.Entity.Order;

import lombok.Data;

@Data
public class OrderSummaryDto {
	private Long id;
    private String customername;
    private String status;
    private BigDecimal totalprice;
    
    public OrderSummaryDto toDto(Order order) {
        OrderSummaryDto dto = new OrderSummaryDto();
        dto.setId(order.getId());
        dto.setCustomername(order.getCustomername());
        dto.setStatus(order.getStatus());
        dto.setTotalprice(order.getTotalprice());
        return dto;
    }
}
