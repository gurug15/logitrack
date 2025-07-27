package com.project.logitrack.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.repositories.ShipmentRepository;

@Service
public class ShipmentServiceImp implements ShipmentService{
	@Autowired
    private ShipmentRepository shipmentRepository;

    @Override
    public Shipment createShipment(Shipment shipment) {
        // createdAt and updatedAt handled by @PrePersist in entity
        // You can add additional business logic here before saving
        return shipmentRepository.save(shipment);
    }

    @Override
    public Optional<Shipment> getShipmentByTrackingId(String trackingId) {
        return shipmentRepository.findByTrackingId(trackingId);
    }

    @Override
    public Optional<Shipment> updateShipmentStatus(Long shipmentId, String status) {
        Optional<Shipment> shipmentOptional = shipmentRepository.findById(shipmentId);
        if(shipmentOptional.isPresent()){
            Shipment shipment = shipmentOptional.get();
            shipment.setStatus(status);
            shipmentRepository.save(shipment);
            return Optional.of(shipment);
        }
        return Optional.empty();
    }

    @Override
    public List<Shipment> getShipmentsByCurrentCenter(Long centerId) {
        return shipmentRepository.findByCurrentCenterId(centerId);
    }
}
