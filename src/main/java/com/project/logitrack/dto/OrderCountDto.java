package com.project.logitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCountDto {
	//no validation needed
	private Long totalOrders;
	private Long pendingOrders;
	private Long  processingOrders;
	private Long deleviredOrders;
}
