package com.project.logitrack.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.Order;
import com.project.logitrack.Entity.User;
import com.project.logitrack.Mappers.OrderMapper;
import com.project.logitrack.dto.OrderCountDto;
import com.project.logitrack.dto.OrderFormDto;
import com.project.logitrack.repositories.ItemRepository;
import com.project.logitrack.repositories.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ItemRepository itemRepository;  //removed from orderController
	
	
	@Override
	public Order createOrder(OrderFormDto orderFormDto, User user) {
	    Order order = OrderMapper.toOrderEntity(orderFormDto, user, itemRepository);
	    Order savedOrder = orderRepository.save(order);
	    return savedOrder;
	}
	
	@Override
    public Order saveOrder(Order order) {
        if (order.getId() == null) {
            order.setCreatedAt(LocalDateTime.now());
            if (order.getStatus() == null) {
                order.setStatus("pending");
            }
        }
        order.setUpdatedAt(LocalDateTime.now());
        // The MOST IMPORTANT STEP: Save the changes to the database
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
	public Optional<Order> getOrderByOrderId(Long Id) {  //included optional here  //this is important
	    return orderRepository.findById(Id);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
	
	@Override
    public OrderCountDto getOrderStatsByUserId(Long userId) {
        return orderRepository.getOrderStatsByUserId(userId);
    }

    @Override
    public List<Order> getRecentOrdersByUserId(Long userId) {
        return orderRepository.findOrdersByUserIdOrderByOrderdateDesc(userId);
    }
    
	
}
