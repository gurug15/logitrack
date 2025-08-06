package com.project.logitrack.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.Mappers.OrderMapper;
import com.project.logitrack.dto.OrderCountDto;
import com.project.logitrack.dto.OrderDto;
import com.project.logitrack.dto.OrderFormDto;
import com.project.logitrack.dto.OrderItemDto;
import com.project.logitrack.dto.OrderViewDto;
import com.project.logitrack.repositories.ItemRepository;
import com.project.logitrack.service.OrderService;
import com.project.logitrack.service.UserService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
    private OrderService orderService;
	

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody OrderFormDto orderFormdto, @AuthenticationPrincipal UserPrinciple currentUser) {
		User user = currentUser.getUser(); 
	    Order savedOrder = orderService.createOrder(orderFormdto, user);
	    return ResponseEntity.ok(savedOrder);
	}

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {  
        List<Order> orders = orderService.getAllOrders(); // or apply filters as you have
        List<OrderDto> dtoList = OrderMapper.toOrderDtoList(orders);
        return ResponseEntity.ok(dtoList);
    }
    
    @GetMapping("/admin")
    public ResponseEntity<List<OrderViewDto>> getAdminOrders() {  //working fine : localhost:4000/orders
        List<Order> orders = orderService.getAllOrders(); // or apply filters as you have
        List<OrderViewDto> dtoList  = orders.stream().map(OrderMapper::toDtoAdminPage).toList();
        return ResponseEntity.ok(dtoList);
    }
    
    
    @GetMapping("/{id}") //working
    public ResponseEntity<OrderViewDto> getOrderById(@PathVariable("id") Long id) {
        Optional<Order> orderOpt = orderService.getOrderByOrderId(id);
        return orderOpt
               .map(OrderMapper::toDtoSummary)   // using method reference
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        Optional<Order> orderOpt = orderService.getOrderByOrderId(id);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Order order = orderOpt.get();
        order.setStatus(status);
        Order savedOrder = orderService.saveOrder(order); 
        return ResponseEntity.ok(OrderMapper.toDtoOrder(savedOrder));
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
