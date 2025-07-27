package com.project.logitrack.service;

import java.util.List;
import java.util.Optional;

import com.project.logitrack.Entity.Order;

public interface OrderService {
	public List<Order> getUserOrders(Long userId);
	public List<Order> getOrdersByStatus(String status);
	public List<Order> getOrdersByCity(String city);
	public List<Order> getOrdersByPostalcode(String Postalcode);
	public Optional<Order> getOrderByOrderId(Long Id);
	public Order createOrder(Order order);
	public List<Order> getAllOrders();
	
}
