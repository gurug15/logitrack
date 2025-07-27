package com.project.logitrack.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.logitrack.Entity.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{
	
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findOrdersByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> findByStatus(@Param("status") String status);

    @Query("SELECT o FROM Order o WHERE o.city = :city")
    List<Order> findByCity(@Param("city") String city);
    
    @Query("SELECT o FROM Order o WHERE o.postalcode = :postalcode")
    List<Order> findByPostalcode(@Param("postalcode") String postalcode);

    Order findById(long id);
}
