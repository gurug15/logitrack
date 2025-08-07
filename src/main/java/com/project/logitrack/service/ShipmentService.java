package com.project.logitrack.service;

import java.util.List;
import java.util.Optional;

import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.dto.ShipmentDto;
import com.project.logitrack.dto.UpdateShipmentRequestDto;

public interface ShipmentService {
	Shipment createShipment(Shipment shipment);

    Optional<Shipment> getShipmentByTrackingId(String trackingId);

    Optional<Shipment> updateShipmentStatus(Long shipmentId, String status);

    List<Shipment> getShipmentsByCurrentCenter(Long centerId);
    ShipmentDto updateShipmentBySubAdmin(Long shipmentId, UpdateShipmentRequestDto request, UserPrinciple currentUser);
    
//    void processNewShipment(Shipment newShipment);// added this new method
    
}
