package com.project.logitrack.service;

import java.util.List;
import java.util.Optional;

import com.project.logitrack.Entity.Order;
import com.project.logitrack.Entity.User;
import com.project.logitrack.dto.OrderCountDto;
import com.project.logitrack.dto.OrderFormDto;

public interface OrderService {
	public List<Order> getUserOrders(Long userId);
	public List<Order> getOrdersByStatus(String status);
	public List<Order> getOrdersByCity(String city);
	public List<Order> getOrdersByPostalcode(String Postalcode);
	public Optional<Order> getOrderByOrderId(Long Id);
	public Order createOrder(OrderFormDto orderFormDto, User user); //changed for creating the order
	public Order saveOrder(Order order); 
    
	public List<Order> getAllOrders();
	
	OrderCountDto getOrderStatsByUserId(Long userId);

    List<Order> getRecentOrdersByUserId(Long userId);
	
}
