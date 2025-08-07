package com.project.logitrack.dto;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class TrackingHistoryDto {
	private Long id;
    private String status;
    private OffsetDateTime timestamp;
    private String notes;
    private String location;
}
