package com.project.logitrack.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ShipmentTrackingDto {
    // Fields for the "Package Details" component
    private String trackingId;
    private String status;
    private String senderName;
    private String recipientName;
    private String deliveryAddress;
    private OffsetDateTime expectedDelivery;
    private BigDecimal weight;
    private String dimensions;

    // Field for the "Progress Tracker" component
    private List<TrackingHistoryDto> trackingHistory;
}