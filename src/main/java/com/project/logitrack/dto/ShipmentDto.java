package com.project.logitrack.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ShipmentDto {
	private Long id;
    private String trackingId;
    private String status;
    private BigDecimal weight;
    private String dimensions;
    private OffsetDateTime expectedDelivery;
    private OffsetDateTime actualDelivery;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Long orderId;
    private Long sourceCenterId;
    private Long destCenterId;
    private Long currentCenterId;
    private String CurrentCenterName;
    private List<TrackingHistoryDto> trackingHistory;
}
