package com.project.logitrack.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.Order;
import com.project.logitrack.repositories.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Order createOrder(Order order) {
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        // Set default order status if not set
        if (order.getStatus() == null) {
            order.setStatus("pending");
        }
        return orderRepository.save(order);
    }
	
	@Override
	public List<Order> getUserOrders(Long userId) {
	    return orderRepository.findOrdersByUserId(userId);
	}
	@Override
	public List<Order> getOrdersByStatus(String status) {
	    return orderRepository.findByStatus(status);
	}
	@Override
	public List<Order> getOrdersByCity(String city) {
	    return orderRepository.findByCity(city);
	}
	@Override
	public List<Order> getOrdersByPostalcode(String Postalcode) {
	    return orderRepository.findByPostalcode(Postalcode);
	}
	@Override
	public Optional<Order> getOrderByOrderId(Long Id) {  //included optional here
	    return orderRepository.findById(Id);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
	
}
