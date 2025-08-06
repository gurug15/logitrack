package com.project.logitrack.service;

import java.util.List;
import java.util.Optional;

import com.project.logitrack.Entity.Order;
import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.dto.OrderCountDto;

public interface OrderService {
	public Optional<Order> getOrderByOrderId(Long Id);
	public Order createOrder(Order order);
	public List<Order> getAllOrders();
	List<Order> getOrdersBySourceCenter(Long centerId);
	OrderCountDto getOrderStatsByUserId(Long userId);

    List<Order> getRecentOrdersByUserId(Long userId);
	Shipment createShipmentFromOrder(Long orderId, UserPrinciple currentUser);
}
