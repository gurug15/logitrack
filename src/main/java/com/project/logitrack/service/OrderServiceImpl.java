package com.project.logitrack.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.LogisticCenter;
import com.project.logitrack.Entity.Order;

import com.project.logitrack.Entity.User;
import com.project.logitrack.Mappers.OrderMapper;

import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.TrackingHistory;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.dto.OrderCountDto;
import com.project.logitrack.dto.OrderFormDto;
import com.project.logitrack.repositories.ItemRepository;
import com.project.logitrack.repositories.LogisticCenterRepository;
import com.project.logitrack.repositories.OrderRepository;
import com.project.logitrack.repositories.ShipmentRepository;
import com.project.logitrack.repositories.TrackingHistoryRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	

	@Autowired
	private ItemRepository itemRepository;  //removed from orderController
	
	

	
	@Autowired
    private ShipmentRepository shipmentRepository; // <-- Add this
	
	@Autowired
	private LogisticCenterRepository logisticCenterRepository;
	
	
	@Autowired
	private LogisticCenterService logisticCenterService;
	
//    @Autowired
//    private TrackingHistoryRepository trackingHistoryRepository; // <-- Add this

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
	    public List<Order> getOrdersBySourceCenter(Long centerId) {
	        // This calls the new method we created in the OrderRepository.
	        return orderRepository.findOrdersByUsersLogisticCenterId(centerId);
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
   
    @Override
    @Transactional
    public Shipment createShipmentFromOrder(Long orderId, UserPrinciple currentUser) {
        // 1. Get Details & Find the Order
        User subAdmin = currentUser.getUser();
        Long subAdminCenterId = subAdmin.getLogisticCenterId().getId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // 2. Perform Security & Validation Checks
        if (order.getUser().getLogisticCenterId() == null || !order.getUser().getLogisticCenterId().getId().equals(subAdminCenterId)) {
            throw new AccessDeniedException("Access Denied: You can only process orders from your own center.");
        }
        if (!"pending".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("This order has already been processed.");
        }

        // 3. Use the Routing Engine to find the Destination Center
        String destinationPincode = order.getPostalcode();
        int destCenterIdInt = logisticCenterService.getCenterFromPincode(destinationPincode);
        if (destCenterIdInt == -1) {
            throw new RuntimeException("Could not determine a valid logistic center for pincode: " + destinationPincode);
        }
        Long destinationCenterId = (long) destCenterIdInt;
        LogisticCenter destinationCenter = logisticCenterRepository.findById(destinationCenterId)
                .orElseThrow(() -> new RuntimeException("Destination center not found with ID: " + destinationCenterId));

        // 4. Create the new Shipment
        Shipment newShipment = new Shipment();
        newShipment.setTrackingId("TRK" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        newShipment.setStatus("Pending");
        newShipment.setOrder(order);
        newShipment.setSourceCenter(order.getUser().getLogisticCenterId());
        newShipment.setCurrentCenter(order.getUser().getLogisticCenterId());
        newShipment.setDestCenter(destinationCenter);
        
        // Calculate total weight from order items (or use a placeholder)
        BigDecimal totalWeight = order.getOrderItems().stream()
                                    .map(item -> item.getItem().getWeight().multiply(new BigDecimal(item.getQuantity())))
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        newShipment.setWeight(totalWeight.max(new BigDecimal("0.5"))); // Ensure a minimum weight
        newShipment.setDimensions("Standard Box");
        newShipment.setExpectedDelivery(order.getExpectedDeliveryDate() != null ? order.getExpectedDeliveryDate().atStartOfDay().atOffset(OffsetDateTime.now().getOffset()) : null);
        // 5. Update the Order's status
        order.setStatus("Processed");
        orderRepository.save(order);

        // 6. Create the first Tracking History record
        TrackingHistory historyRecord = new TrackingHistory();
        historyRecord.setShipment(newShipment);
        historyRecord.setStatus("Pending");
        historyRecord.setNotes("Shipment created from order #" + order.getId() + " at " + newShipment.getSourceCenter().getName());
        historyRecord.setCenter(newShipment.getSourceCenter());
        historyRecord.setUpdatedByUser(subAdmin);

        // Save the new shipment. The TrackingHistory will be saved automatically due to the Cascade setting on the Shipment entity.
        return shipmentRepository.save(newShipment);
    }
    
    
}
