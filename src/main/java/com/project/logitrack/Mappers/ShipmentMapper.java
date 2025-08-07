package com.project.logitrack.Mappers;

import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.TrackingHistory;
import com.project.logitrack.dto.ShipmentDto;
import com.project.logitrack.dto.TrackingHistoryDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShipmentMapper {

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

        // This now correctly calls the helper method below
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