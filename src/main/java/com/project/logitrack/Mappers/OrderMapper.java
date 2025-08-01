package com.project.logitrack.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.project.logitrack.Entity.Order;
import com.project.logitrack.Entity.OrderItem;
import com.project.logitrack.dto.OrderDto;
import com.project.logitrack.dto.OrderItemDto;
import com.project.logitrack.dto.OrderSummaryDto;

import lombok.Data;

@Data
public class OrderMapper {
	    public static OrderSummaryDto toDtoSummary(Order order) {
	        if (order == null) {
	            return null;
	        }
	        OrderSummaryDto dto = new OrderSummaryDto();
	        dto.setId(order.getId());
	        dto.setCustomername(order.getCustomername());
	        dto.setStatus(order.getStatus());
	        dto.setTotalprice(order.getTotalprice());
	        // map other needed fields
	        return dto;
	    }
	    public static OrderDto toDtoOrder(Order order) {
	        if (order == null) return null;
	        OrderDto dto = new OrderDto();
	        dto.setId(order.getId());
	        dto.setCustomername(order.getCustomername());
	        dto.setOrderDate(order.getOrderdate());
	        dto.setStatus(order.getStatus());
	        dto.setTotalprice(order.getTotalprice());
	        // fill other fields as needed
	        return dto;
	    }

	    public static OrderItemDto toDtoOrderItem(OrderItem item) {
	        if (item == null) return null;
	        OrderItemDto dto = new OrderItemDto();
	        dto.setId(item.getId());
	        dto.setProductName(item.getProductName());
	        dto.setQuantity(item.getQuantity());
	        dto.setPrice(item.getUnitPrice());
	        return dto;
	    }

	    public static List<OrderItemDto> toOrderItemDtoList(List<OrderItem> items) {
	        if (items == null) return Collections.emptyList();
	        return items.stream()
	                .map(OrderMapper::toDtoOrderItem)
	                .collect(Collectors.toList());
	    }
		public static List<OrderDto> toOrderDtoList(List<Order> orders) {
			if(orders==null) return Collections.emptyList();
			return orders.stream().map(OrderMapper::toDtoOrder).collect(Collectors.toList());
		}

}
