package com.project.logitrack.service;

import java.util.List;
import java.util.Optional;

import com.project.logitrack.Entity.Shipment;

public interface ShipmentService {
	Shipment createShipment(Shipment shipment);

    Optional<Shipment> getShipmentByTrackingId(String trackingId);

    Optional<Shipment> updateShipmentStatus(Long shipmentId, String status);

    List<Shipment> getShipmentsByCurrentCenter(Long centerId);
}
