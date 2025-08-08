package com.project.logitrack.Mappers;

import com.project.logitrack.Entity.Order;
import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.TrackingHistory;
import com.project.logitrack.dto.ShipmentDto;
import com.project.logitrack.dto.ShipmentTrackingDto;
import com.project.logitrack.dto.TrackingHistoryDto;
import com.project.logitrack.repositories.LogisticCenterRepository;
import com.project.logitrack.service.LogisticCenterService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component
public class ShipmentMapper {
		
		
	private static LogisticCenterService logisticCenterService;
    private static LogisticCenterRepository logisticCenterRepository;

    @Autowired
    public void setStaticServices(LogisticCenterService lcs, LogisticCenterRepository lcr) {
        ShipmentMapper.logisticCenterService = lcs;
        ShipmentMapper.logisticCenterRepository = lcr;
    }
	
    public static ShipmentDto toDto(Shipment shipment) {
        if (shipment == null) return null;
        ShipmentDto dto = new ShipmentDto();
        
        dto.setId(shipment.getId());
        dto.setTrackingId(shipment.getTrackingId());
        dto.setStatus(shipment.getStatus());
        dto.setWeight(shipment.getWeight());
        dto.setDimensions(shipment.getDimensions());
        dto.setExpectedDelivery(shipment.getExpectedDelivery());
        dto.setActualDelivery(shipment.getActualDelivery());
        dto.setCreatedAt(shipment.getCreatedAt());
        dto.setUpdatedAt(shipment.getUpdatedAt());
        dto.setOrderId(shipment.getOrder() != null ? shipment.getOrder().getId() : null);

        if (shipment.getSourceCenter() != null) {
            dto.setSourceCenterId(shipment.getSourceCenter().getId());
            dto.setSourceCenterName(shipment.getSourceCenter().getName());
        }
        if (shipment.getDestCenter() != null) {
            dto.setDestCenterId(shipment.getDestCenter().getId());
            dto.setDestCenterName(shipment.getDestCenter().getName());
        }
        if (shipment.getCurrentCenter() != null) {
            dto.setCurrentCenterId(shipment.getCurrentCenter().getId());
            dto.setCurrentCenterName(shipment.getCurrentCenter().getName());
        }
        	
        if (shipment.getCurrentCenter() != null && shipment.getDestCenter() != null &&
                shipment.getSourceCenter() != null) {
                
                if (shipment.getCurrentCenter().getId().equals(shipment.getDestCenter().getId())) {
                    dto.setNextCenterName("Out for Final Delivery");
                } else {
                    try {
                        List<Integer> route = logisticCenterService.findRoute(
                            shipment.getSourceCenter().getId().intValue(),
                            shipment.getDestCenter().getId().intValue()
                        );
                        int currentIndex = route.indexOf(shipment.getCurrentCenter().getId().intValue());
                        if (currentIndex != -1 && currentIndex + 1 < route.size()) {
                            Long nextCenterId = (long) route.get(currentIndex + 1);
                            logisticCenterRepository.findById(nextCenterId).ifPresent(nextCenter -> {
                                dto.setNextCenterName(nextCenter.getName());
                            });
                        } else {
                            dto.setNextCenterName("Final Destination Reached");
                        }
                    } catch (Exception e) {
                        dto.setNextCenterName("N/A");
                    }
                }
        	
        }
        // This now correctly calls the helper method below
        dto.setTrackingHistory(toTrackingHistoryDtoList(shipment.getTrackingHistory()));
        
        return dto;
    }
    
    
    public static ShipmentTrackingDto toShipmentTrackingDto(Shipment shipment) {
        if (shipment == null) return null;

        ShipmentTrackingDto dto = new ShipmentTrackingDto();
        Order order = shipment.getOrder(); // Get the associated order

        // Populate shipment details
        dto.setTrackingId(shipment.getTrackingId());
        dto.setStatus(shipment.getStatus());
        dto.setExpectedDelivery(shipment.getExpectedDelivery());
        dto.setWeight(shipment.getWeight());
        dto.setDimensions(shipment.getDimensions());

        // Populate details from the associated Order
        if (order != null) {
            dto.setRecipientName(order.getCustomername());
            dto.setDeliveryAddress(
                String.format("%s, %s, %s, %s",
                    order.getCustomeraddress(),
                    order.getCity(),
                    order.getState(),
                    order.getPostalcode()
                )
            );
            // Get the sender's name from the user who created the order
            if (order.getUser() != null) {
                dto.setSenderName(order.getUser().getName());
            }
        }

        // Use the existing helper to map the tracking history
        dto.setTrackingHistory(toTrackingHistoryDtoList(shipment.getTrackingHistory()));

        return dto;
    }
    
    
    
    // This method now correctly calls the toTrackingHistoryDto helper
    public static List<TrackingHistoryDto> toTrackingHistoryDtoList(List<TrackingHistory> historyList) {
        if (historyList == null) return Collections.emptyList();
        return historyList.stream()
            .map(ShipmentMapper::toTrackingHistoryDto) // <-- FIX: Was calling the wrong method
            .collect(Collectors.toList());
    }
	
    // This helper method now includes all the data needed for the UI
    public static TrackingHistoryDto toTrackingHistoryDto(TrackingHistory history) {
        if (history == null) return null;
        TrackingHistoryDto dto = new TrackingHistoryDto();
        dto.setId(history.getId());
        dto.setStatus(history.getStatus());
        dto.setNotes(history.getNotes()); // <-- ADDED for UI
        dto.setTimestamp(history.getTimestamp());
        if (history.getCenter() != null) {
            dto.setLocation(history.getCenter().getName()); // <-- ADDED for UI
        }
        return dto;
    }

	public static List<ShipmentDto> toDtoList(List<Shipment> shipmentsResult) {
		if(shipmentsResult == null) return Collections.emptyList();
		return shipmentsResult.stream().map(ShipmentMapper::toDto).collect(Collectors.toList());
	}
}