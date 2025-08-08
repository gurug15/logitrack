package com.project.logitrack.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.logitrack.Entity.Order;
import com.project.logitrack.dto.OrderCountDto;

public interface OrderRepository extends JpaRepository<Order,Long>{
	
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findOrdersByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> findByStatus(@Param("status") String status);

    @Query("SELECT o FROM Order o WHERE o.city = :city")
    List<Order> findByCity(@Param("city") String city);
    
    @Query("SELECT o FROM Order o WHERE o.postalcode = :postalcode")
    List<Order> findByPostalcode(@Param("postalcode") String postalcode);
    
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    List<Order> findOrdersByUserIdOrderByOrderdateDesc(@Param("userId") Long userId);
    
    @Query(value = """
    	    SELECT 
    	      COUNT(*) as total_orders,
    	      COUNT(*) FILTER (WHERE status = 'pending') as pending_orders,
    	      COUNT(*) FILTER (WHERE status = 'processed') as processing_orders,
    	      COUNT(*) FILTER (WHERE status = 'delivered') as delivered_orders
    	    FROM orders
    	    WHERE userid = :userId
    	    """, nativeQuery = true)
    OrderCountDto getOrderStatsByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.logisticCenterId.id = :centerId")
    List<Order> findOrdersByUsersLogisticCenterId(@Param("centerId") Long centerId);
    
}
