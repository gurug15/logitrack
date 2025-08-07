package com.project.logitrack.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.LogisticCenter;
import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.TrackingHistory;
import com.project.logitrack.Entity.User;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.Mappers.ShipmentMapper;
import com.project.logitrack.dto.ShipmentDto;
import com.project.logitrack.dto.UpdateShipmentRequestDto;
import com.project.logitrack.repositories.LogisticCenterRepository;
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
	private LogisticCenterRepository logisticCenterRepository;
	
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
    @Transactional
    public ShipmentDto updateShipmentBySubAdmin(Long shipmentId, UpdateShipmentRequestDto request, UserPrinciple currentUser) {
        User subAdmin = currentUser.getUser();
        Long subAdminCenterId = subAdmin.getLogisticCenterId().getId();

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with ID: " + shipmentId));

        // Security Check
        if (shipment.getCurrentCenter() == null || !shipment.getCurrentCenter().getId().equals(subAdminCenterId)) {
            throw new AccessDeniedException("Access Denied: You can only update shipments at your own center.");
        }

        String requestedStatus = request.getStatus();
        String notes;

        // --- NEW, SMARTER AUTOMATED LOGIC ---

        // First, check if the shipment is already at its final destination
        if (shipment.getCurrentCenter().getId().equals(shipment.getDestCenter().getId())) {
            // If it's at the final center, the only valid next step is delivery.
            if ("Delivered".equalsIgnoreCase(requestedStatus)) {
                shipment.setStatus("Delivered");
                shipment.setActualDelivery(OffsetDateTime.now());
                notes = "Shipment has been successfully delivered.";
            } else {
                shipment.setStatus("Out for Delivery");
                notes = "Shipment is now out for final delivery.";
            }
        }
        // If the shipment is NOT at its final destination, and the user wants to dispatch it...
        else if ("In Transit".equalsIgnoreCase(requestedStatus)) {
            int sourceId = shipment.getSourceCenter().getId().intValue();
            int destId = shipment.getDestCenter().getId().intValue();
            
            // 1. Get the route from your friend's service
            List<Integer> route = logisticCenterService.findRoute(sourceId, destId);
            int currentIndexInRoute = route.indexOf(subAdminCenterId.intValue());

            // 2. Determine the next stop
            if (currentIndexInRoute != -1 && currentIndexInRoute + 1 < route.size()) {
                Long nextCenterId = (long) route.get(currentIndexInRoute + 1);
                LogisticCenter nextCenter = logisticCenterRepository.findById(nextCenterId)
                        .orElseThrow(() -> new RuntimeException("Next center in route not found: " + nextCenterId));
                
                // 3. Update the shipment's current location - THIS IS THE KEY FIX
                shipment.setCurrentCenter(nextCenter);
                notes = "Status: In Transit. Dispatched from " + subAdmin.getLogisticCenterId().getName() + " and forwarded to " + nextCenter.getName() + ".";
                shipment.setStatus("In Transit");

            } else {
                // This case should rarely happen, but it's a safe fallback.
                notes = "Status updated to '" + requestedStatus + "'. Could not determine next center in route.";
                shipment.setStatus(requestedStatus);
            }
        } else {
            // For any other manual status update (e.g., "Delayed")
            shipment.setStatus(requestedStatus);
            notes = "Status manually updated to '" + requestedStatus + "'";
        }
        
        // Create the tracking history record
        TrackingHistory historyRecord = new TrackingHistory();
        historyRecord.setShipment(shipment);
        historyRecord.setStatus(shipment.getStatus()); 
        historyRecord.setNotes(notes);
        historyRecord.setCenter(subAdmin.getLogisticCenterId()); 
        historyRecord.setUpdatedByUser(subAdmin);
        
        trackingHistoryRepository.save(historyRecord);

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
