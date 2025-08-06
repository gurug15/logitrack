package com.project.logitrack.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.TrackingHistory;
import com.project.logitrack.Entity.User;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.Mappers.ShipmentMapper;
import com.project.logitrack.dto.ShipmentDto;
import com.project.logitrack.repositories.ShipmentRepository;
import com.project.logitrack.repositories.TrackingHistoryRepository;

import jakarta.transaction.Transactional;

@Service
public class ShipmentServiceImp implements ShipmentService{
	@Autowired
    private ShipmentRepository shipmentRepository;
	
	@Autowired
    private LogisticCenterService logisticCenterService; //for routing
	
	@Autowired
	private TrackingHistoryRepository trackingHistoryRepository;

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
    
    @Override
    @Transactional // Ensures this whole operation is a single, safe database transaction.
    public ShipmentDto updateShipmentStatusBySubAdmin(Long shipmentId, String newStatus, UserPrinciple currentUser) {
        // 1. Get the sub-admin's details from the security principal.
        User subAdmin = currentUser.getUser();
        Long subAdminCenterId = subAdmin.getLogisticCenterId().getId();

        // 2. Find the shipment by its ID. If it's not found, throw an error.
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with ID: " + shipmentId));

        // 3. !! THE CRITICAL SECURITY CHECK !!
        // This ensures a sub-admin can only update packages at their OWN center.
        if (shipment.getCurrentCenter() == null || !shipment.getCurrentCenter().getId().equals(subAdminCenterId)) {
            // If the shipment is not at their center, deny access.
            throw new AccessDeniedException("Access Denied: You can only update shipments at your own center.");
        }

        // 4. Update the shipment's status.
        shipment.setStatus(newStatus);

        // 5. Create a new tracking history record to create an audit trail of this change.
        TrackingHistory historyRecord = new TrackingHistory();
        historyRecord.setShipment(shipment);
        historyRecord.setStatus(newStatus);
        historyRecord.setCenter(shipment.getCurrentCenter());
        historyRecord.setUpdatedByUser(subAdmin);
        historyRecord.setTimestamp(OffsetDateTime.now());
        
        trackingHistoryRepository.save(historyRecord);
        
        // Because of @Transactional, JPA automatically saves the changes to the 'shipment' object.
        // We return a DTO to the controller for a clean API response.
        return ShipmentMapper.toDto(shipment);
    }
    
    
//    @Override
//    @Transactional // Ensures these steps happen together or not at all.
//    public void processNewShipment(Shipment newShipment) {
//        // 1. Get the source pincode from the shipment data.
//        String sourcePincode = newShipment.getSourcePincode(); //I got problem here 
//
//        // 2. Delegate to LogisticCenterService to find the starting center ID.
//        int sourceCenterId = logisticCenterService.getCenterFromPincode(sourcePincode);
//
//        // 3. Find the actual LogisticCenter object.
//        // In a real app, you'd fetch this from the database. For this system, we can assume it exists.
//        // For now, let's just log this information.
//        // NOTE: You would need a method in LogisticCenterService to get a center by its ID.
//        // LogisticCenter sourceCenter = logisticCenterService.getCenterById((long) sourceCenterId);
//        
//        // 4. Update the shipment's *current location* to this starting center.
//        // THIS IS A CRITICAL STEP. The shipment is now officially "at" a location.
//        // newShipment.setCurrentCenter(sourceCenter);
//        // Note: This step requires that your Shipment entity can have its currentCenter updated and saved.
//
//        // 5. Create the VERY FIRST tracking history record.
//        TrackingHistory initialRecord = new TrackingHistory();
//        initialRecord.setShipment(newShipment);
//        initialRecord.setStatus("PROCESSING");
//        initialRecord.setStatusCode("PROCESSING");
//        initialRecord.setNotes("Shipment created and registered at origin center.");
//        // initialRecord.setCenter(sourceCenter); // Set the center where the event happened.
//        
//        // The @PrePersist in TrackingHistory will handle the timestamps.
//
//        trackingHistoryRepository.save(initialRecord);
//        // shipmentRepository.save(newShipment); // Save the shipment again to update its currentCenter
//    }
}
