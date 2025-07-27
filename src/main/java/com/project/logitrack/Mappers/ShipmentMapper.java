package com.project.logitrack.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.TrackingHistory;
import com.project.logitrack.dto.ShipmentDto;
import com.project.logitrack.dto.TrackingHistoryDto;

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
        dto.setSourceCenterId(shipment.getSourceCenter() != null ? shipment.getSourceCenter().getId() : null);
        dto.setDestCenterId(shipment.getDestCenter() != null ? shipment.getDestCenter().getId() : null);
        dto.setCurrentCenterId(shipment.getCurrentCenter() != null ? shipment.getCurrentCenter().getId() : null);
        dto.setTrackingHistory(toHistoryDtoList(shipment.getTrackingHistory()));
        return dto;
    }
    public static List<TrackingHistoryDto> toHistoryDtoList(List<TrackingHistory> list) {
        if (list == null) return null;
        return list.stream()
            .map(ShipmentMapper::toDto)
            .collect(Collectors.toList());
    }
    public static TrackingHistoryDto toDto(TrackingHistory h) {
        if (h == null) return null;
        TrackingHistoryDto dto = new TrackingHistoryDto();
        dto.setId(h.getId());
        dto.setStatus(h.getStatus());
        dto.setTimestamp(h.getTimestamp());
        dto.setLocation(h.getLocation());
        return dto;
    }
	public static List<ShipmentDto> toDtoList(List<Shipment> shipmentsResult) {
		if(shipmentsResult==null) return Collections.emptyList();
		return shipmentsResult.stream().map(ShipmentMapper::toDto).collect(Collectors.toList());
	}
}
