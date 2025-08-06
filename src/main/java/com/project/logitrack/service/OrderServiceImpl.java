package com.project.logitrack.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.Order;
import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.User;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.dto.OrderCountDto;
import com.project.logitrack.repositories.OrderRepository;
import com.project.logitrack.repositories.ShipmentRepository;
import com.project.logitrack.repositories.TrackingHistoryRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@Autowired
    private ShipmentRepository shipmentRepository; // <-- Add this

    @Autowired
    private TrackingHistoryRepository trackingHistoryRepository; // <-- Add this

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
	    public List<Order> getOrdersBySourceCenter(Long centerId) {
	        // This calls the new method we created in the OrderRepository.
	        return orderRepository.findOrdersByUsersLogisticCenterId(centerId);
	    }
	@Override
	public Optional<Order> getOrderByOrderId(Long Id) {  //included optional here
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
    
    @Override
    @Transactional
    public Shipment createShipmentFromOrder(Long orderId, UserPrinciple currentUser) {
        User subAdmin = currentUser.getUser();
        Long subAdminCenterId = subAdmin.getLogisticCenterId().getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Security & Validation Checks
        if (order.getUser() == null || order.getUser().getLogisticCenterId() == null || !order.getUser().getLogisticCenterId().getId().equals(subAdminCenterId)) {
            throw new AccessDeniedException("Access Denied: You can only process orders originating from your own center.");
        }
        if (!"pending".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("This order has already been processed and cannot be shipped again.");
        }

        // Create the new Shipment
        Shipment newShipment = new Shipment();
        newShipment.setTrackingId("TRK" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        newShipment.setStatus("Pending");
        newShipment.setOrder(order);
        newShipment.setSourceCenter(order.getUser().getLogisticCenterId());
        newShipment.setCurrentCenter(order.getUser().getLogisticCenterId());
        
        // This is a placeholder; you'll likely have logic to determine the destination center
        newShipment.setDestCenter(order.getUser().getLogisticCenterId()); 

        newShipment.setExpectedDelivery(order.getExpectedDeliveryDate() != null ? order.getExpectedDeliveryDate().atStartOfDay().atOffset(OffsetDateTime.now().getOffset()) : null);
        newShipment.setWeight(new BigDecimal("1.0")); // Placeholder
        newShipment.setDimensions("Standard Box"); // Placeholder

        // Update the original Order's status
        order.setStatus("Processed");
        orderRepository.save(order);
        
        // Save the new shipment. Because of @Transactional, if any step fails, everything is rolled back.
        return shipmentRepository.save(newShipment);
    }
}
