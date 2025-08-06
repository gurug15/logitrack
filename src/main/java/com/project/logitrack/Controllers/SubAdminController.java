package com.project.logitrack.Controllers;


import com.project.logitrack.Entity.Order;
import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.Mappers.OrderMapper;
import com.project.logitrack.Mappers.ShipmentMapper;
import com.project.logitrack.dto.OrderDto;
import com.project.logitrack.dto.ShipmentDto;
import com.project.logitrack.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subadmin")
public class SubAdminController {

    @Autowired
    private OrderService orderService;
    // You may also need your ShipmentService here later

    // --- ADD THIS ENTIRE NEW METHOD ---
    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('sub_admin')") // Ensures only sub-admins can call this
    public ResponseEntity<List<OrderDto>> getIncomingOrders(@AuthenticationPrincipal UserPrinciple currentUser) {
        
        // 1. Securely get the logged-in sub-admin's logistic center ID from the JWT.
        Long centerId = currentUser.getUser().getLogisticCenterId().getId();

        // 2. Call the service method we created to get the orders for that specific center.
        List<Order> orders = orderService.getOrdersBySourceCenter(centerId);
        
        // 3. Map the results to a DTO list for a clean frontend response.
        List<OrderDto> orderDtos = OrderMapper.toOrderDtoList(orders);
        
        return ResponseEntity.ok(orderDtos);
    }
    
    @PostMapping("/orders/{orderId}/create-shipment")
    @PreAuthorize("hasAuthority('sub_admin')") // Ensures only sub-admins can call
    public ResponseEntity<ShipmentDto> createShipmentFromOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrinciple currentUser) {

       
        Shipment newShipment = orderService.createShipmentFromOrder(orderId, currentUser);

        ShipmentDto shipmentDto = ShipmentMapper.toDto(newShipment);
        return new ResponseEntity<>(shipmentDto, HttpStatus.CREATED);
    }
}