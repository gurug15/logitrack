package com.project.logitrack.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.dto.ShipmentDto;
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
    
//    @Override
//    public List<ShipmentDto> getShipmentsForCenter(UserPrinciple currentUser) {
//        // 1. Get the logistic center ID from the logged-in subadmin
//        Long centerId = currentUser.getLogisticCenterId();
//
//        if (centerId == null) {
//            // Or handle as an error if a subadmin must have a center
//            return List.of(); 
//        }
//
//        // 2. Fetch shipments from the repository
//        List<Shipment> shipments = shipmentRepository.findByCurrentCenterId(centerId);
//
//        // 3. Map entities to DTOs for the frontend
//        return shipments.stream().map(shipment -> {
//            ShipmentDto dto = new ShipmentDto();
//            dto.setId(shipment.getId());
//            dto.setTrackingId(shipment.getTrackingId());
//            dto.setStatus(shipment.getStatus());
//            dto.setExpectedDelivery(shipment.getExpectedDelivery());
//            
//            // Avoid NullPointerException if center is not set
//            if (shipment.getCurrentCenter() != null) {
//                dto.setCurrentCenterName(shipment.getCurrentCenter().getName());
//            } else {
//                dto.setCurrentCenterName("N/A");
//            }
//            return dto;
//        }).collect(Collectors.toList());
//    }
}
