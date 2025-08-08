package com.project.logitrack.service;

import java.sql.Date;
import java.time.LocalDate;
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
        
        // Check if shipment is already in a final state that cannot be changed
        if ("Delivered".equalsIgnoreCase(shipment.getStatus())) {
            throw new IllegalStateException("Shipment has already been delivered and cannot be updated further.");
        }
        
        String requestedStatus = request.getStatus();
        String notes;

        // PRIORITY 1: Handle "Out for Delivery" → "Delivered" transition
        if ("Out for Delivery".equalsIgnoreCase(shipment.getStatus()) && "Delivered".equalsIgnoreCase(requestedStatus)) {
            shipment.setStatus("Delivered");
            shipment.setActualDelivery(OffsetDateTime.now());
            
            shipment.getOrder().setDeliveredDate(LocalDate.now());
            shipment.getOrder().setStatus(requestedStatus);
            notes = "Shipment has been successfully delivered.";
        }
        // PRIORITY 2: If shipment is at destination center and needs to go out for delivery
        else if (shipment.getCurrentCenter().getId().equals(shipment.getDestCenter().getId()) && 
                 !"Out for Delivery".equalsIgnoreCase(shipment.getStatus()) &&
                 !"Delivered".equalsIgnoreCase(shipment.getStatus())) {
            shipment.setStatus("Out for Delivery");
            notes = "Shipment is now out for final delivery.";
        }
        // PRIORITY 3: Handle routing/dispatch logic for "In Transit"
        else if ("In Transit".equalsIgnoreCase(requestedStatus)) {
            int sourceId = shipment.getSourceCenter().getId().intValue();
            int destId = shipment.getDestCenter().getId().intValue();
            
            // Get the route from routing service
            List<Integer> route = logisticCenterService.findRoute(sourceId, destId);
            int currentIndexInRoute = route.indexOf(subAdminCenterId.intValue());

            // Determine the next stop
            if (currentIndexInRoute != -1 && currentIndexInRoute + 1 < route.size()) {
                Long nextCenterId = (long) route.get(currentIndexInRoute + 1);
                LogisticCenter nextCenter = logisticCenterRepository.findById(nextCenterId)
                        .orElseThrow(() -> new RuntimeException("Next center in route not found: " + nextCenterId));
                
                // Update the shipment's current location
                shipment.setCurrentCenter(nextCenter);
                shipment.setStatus("In Transit");
                notes = "Dispatched from " + subAdmin.getLogisticCenterId().getName() + 
                       " and forwarded to " + nextCenter.getName() + ".";
            } else {
                // Fallback case
                shipment.setStatus(requestedStatus);
                notes = "Status updated to '" + requestedStatus + "'. Could not determine next center in route.";
            }
        }
        // PRIORITY 4: Handle any other manual status updates
        else {
            shipment.setStatus(requestedStatus);
            notes = "Status manually updated to '" + requestedStatus + "' by " + subAdmin.getName();
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
   }