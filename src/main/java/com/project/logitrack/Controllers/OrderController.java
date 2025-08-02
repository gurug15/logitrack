package com.project.logitrack.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.logitrack.Entity.Order;
import com.project.logitrack.Entity.OrderItem;
import com.project.logitrack.Entity.User;
import com.project.logitrack.Mappers.OrderMapper;
import com.project.logitrack.dto.OrderCountDto;
import com.project.logitrack.dto.OrderDto;
import com.project.logitrack.dto.OrderFormDto;
import com.project.logitrack.dto.OrderItemDto;
import com.project.logitrack.dto.OrderSummaryDto;
import com.project.logitrack.dto.OrderViewDto;
import com.project.logitrack.service.OrderService;
import com.project.logitrack.service.UserService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
    private OrderService orderService;
	
	@Autowired
	private UserService userservice;

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody OrderFormDto orderFormdto) {
	    User user = userservice.getUserById((long)1); // get current user, according to your logic
	    //we will get from JWT  //it will not throw exceptions 
	    Order order = OrderMapper.toOrderEntity(orderFormdto, user);
	    Order savedOrder = orderService.createOrder(order); // persist (including OrderItems)
	    return ResponseEntity.ok(savedOrder);
	}

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {  //working fine : localhost:4000/orders
        List<Order> orders = orderService.getAllOrders(); // or apply filters as you have
        List<OrderDto> dtoList = OrderMapper.toOrderDtoList(orders);
        return ResponseEntity.ok(dtoList);
    }
    
    @GetMapping("/{id}") //working
    public ResponseEntity<OrderSummaryDto> getOrderById(@PathVariable("id") Long id) {
        Optional<Order> orderOpt = orderService.getOrderByOrderId(id);
        return orderOpt
               .map(OrderMapper::toDtoSummary)   // using method reference
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
	    @PutMapping("/{id}")    //not tried
	 // @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	 public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
	     Optional<Order> orderOpt = orderService.getOrderByOrderId(id);
	     if (orderOpt.isEmpty()) {
	         return ResponseEntity.notFound().build();
	     }
	     Order order = orderOpt.get();
	     order.setStatus(status);
	     orderService.createOrder(order);  // persist update
	     return ResponseEntity.ok(OrderMapper.toDtoOrder(order));  // return DTO here
	 }
	
	 @GetMapping("/{id}/items")  //working
	 // @PreAuthorize("@orderSecurity.isOwnerOrStaff(authentication, #id)")
	 public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable Long id) {
	     Optional<Order> orderOpt = orderService.getOrderByOrderId(id);
	     if (orderOpt.isEmpty()) {
	         return ResponseEntity.notFound().build();
	     }
	     List<OrderItem> items = orderOpt.get().getOrderItems();
	     return ResponseEntity.ok(OrderMapper.toOrderItemDtoList(items));
	 }
	 
	@GetMapping("/stats/{userId}")//JWT will replace userId
	public ResponseEntity<OrderCountDto> getOrderStats(@PathVariable Long userId) {
	    OrderCountDto stats = orderService.getOrderStatsByUserId(userId);
	    return ResponseEntity.ok(stats);
	}
	
	@GetMapping("/recent/{userId}")//JWT will replace userId
	public ResponseEntity<List<OrderDto>> getRecentOrders(@PathVariable Long userId) {
	    List<Order> orders = orderService.getRecentOrdersByUserId(userId);
	    List<OrderDto> dtoList = OrderMapper.toOrderDtoList(orders);
	    return ResponseEntity.ok(dtoList);
	}
	
	@GetMapping("/view/{orderId}")
    public ResponseEntity<OrderViewDto> getOrderByOrderId(@PathVariable Long orderId) {
        return orderService.getOrderByOrderId(orderId)
            .map(order -> ResponseEntity.ok(OrderMapper.toDtoOrderView(order)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
