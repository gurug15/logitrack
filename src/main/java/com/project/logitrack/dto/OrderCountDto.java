package com.project.logitrack.dto;

import lombok.Data;

@Data
public class OrderCountDto {
	Integer totalOrders;
	Integer pendingOrders;
	Integer  processingOrders;
	Integer deleviredOrders;
}
