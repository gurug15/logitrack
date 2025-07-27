package com.project.logitrack.repositories;

import java.util.List;
import java.util.Optional;

import com.project.logitrack.Entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShipmentRepository extends JpaRepository<Shipment,Long>{
	// Find shipment by trackingId (assuming trackingId is unique)
    Optional<Shipment> findByTrackingId(String trackingId);

    // Find shipments by currentCenter id
    @Query("SELECT s FROM Shipment s WHERE s.currentCenter.id = :centerId")
    List<Shipment> findByCurrentCenterId(@Param("centerId") Long centerId);
}
