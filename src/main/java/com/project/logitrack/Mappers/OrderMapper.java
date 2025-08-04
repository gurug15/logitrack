package com.project.logitrack.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.project.logitrack.Entity.Order;
import com.project.logitrack.Entity.OrderItem;
import com.project.logitrack.Entity.User;
import com.project.logitrack.dto.OrderDto;
import com.project.logitrack.dto.OrderFormDto;
import com.project.logitrack.dto.OrderItemDto;
import com.project.logitrack.dto.OrderItemFormDto;
import com.project.logitrack.dto.OrderSummaryDto;
import com.project.logitrack.dto.OrderViewDto;

import lombok.Data;

@Data
public class OrderMapper {
//		public static BigDecimal totalPrice;
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
	        dto.setTotalPrice(item.getQuantity() * item.getUnitPrice().doubleValue()); //here made some changes
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
		
		public static Order toOrderEntity(OrderFormDto dto, User user) {
	        if (dto == null) return null;

	        Order order = new Order();
	        order.setUser(user); // Pass current authenticated user/entity if needed
	        order.setCustomername(dto.getCustomerName());
	        order.setCustomerphone(dto.getContact());
	        order.setCustomeremail(dto.getEmail());
	        order.setCustomeraddress(dto.getAddress());
	        order.setCity(dto.getCity());
	        order.setState(dto.getState());
	        order.setPostalcode(dto.getPostalCode());
	        order.setCountry(dto.getCountry() != null ? dto.getCountry() : "India");
	        order.setStatus("pending");
	        order.setOrderdate(java.time.LocalDate.now());
	        order.setCreatedAt(java.time.LocalDateTime.now());
	        order.setUpdatedAt(java.time.LocalDateTime.now());
	        
	        
	        
	        // Order items
	        List<OrderItem> items = toOrderItemEntities(dto.getItems(), order);
	        order.setOrderItems(items);

	        return order;
	    }

	    public static List<OrderItem> toOrderItemEntities(List<OrderItemFormDto> dtos, Order order) {
	        if (dtos == null) return java.util.Collections.emptyList();
	        return dtos.stream().map(dto -> {
	            OrderItem item = new OrderItem();
	            item.setOrder(order);
	            item.setProductName(dto.getProductName());
	            item.setQuantity(dto.getQuantity());
	            item.setUnitPrice(dto.getPrice());
	            return item;
	        }).collect(java.util.stream.Collectors.toList());
	    }
	    //from here view methods
	    public static OrderViewDto toDtoOrderView(Order order) {
	        if(order == null) return null;
	        OrderViewDto dto = new OrderViewDto();
	        dto.setId(order.getId());
	        dto.setCustomername(order.getCustomername());
	        dto.setCustomerphone(order.getCustomerphone());
	        dto.setCustomeremail(order.getCustomeremail());
	        dto.setCustomeraddress(order.getCustomeraddress());
	        dto.setCity(order.getCity());
	        dto.setState(order.getState());
	        dto.setPostalcode(order.getPostalcode());
	        dto.setCountry(order.getCountry());
	        
	        dto.setExpectedDeliveryDate(order.getExpectedDeliveryDate());//added to see the expected delivery date
	        dto.setDeliveredDate(order.getDeliveredDate());  //added to see the delivery date
	        dto.setOrderdate(order.getOrderdate());
	        
	        dto.setStatus(order.getStatus());
	        dto.setTotalprice(order.getTotalprice());

	        if(order.getOrderItems() != null) {
	            List<OrderItemDto> orderItemsDto = order.getOrderItems().stream()
	                .map(item -> {
	                   OrderItemDto itemDto = new OrderItemDto();
	                   itemDto.setId(item.getId());
	                   itemDto.setProductName(item.getProductName());
	                   itemDto.setQuantity(item.getQuantity());
	                   itemDto.setPrice(item.getUnitPrice());
	                   itemDto.setTotalPrice(item.getQuantity() * item.getUnitPrice().doubleValue());
	                   return itemDto;
	                }).collect(Collectors.toList());

	            dto.setOrderItems(orderItemsDto);
	        }

	        return dto;
	    }

}
