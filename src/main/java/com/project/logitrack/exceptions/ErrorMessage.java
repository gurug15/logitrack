package com.project.logitrack.exceptions;

import java.time.Instant;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
	public String message;
	public String details;
	public Instant timestamp;
	private Map<String, String> fieldErrors; // Add this
	
	// For normal errors
    public ErrorMessage(String message, String details, Instant timestamp) {
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    // For validation errors
    public ErrorMessage(String message, Map<String, String> fieldErrors, Instant timestamp) {
        this.message = message;
        this.fieldErrors = fieldErrors;
        this.timestamp = timestamp;
    }

    public String getMessage() { return message; }
    public String getDetails() { return details; }
    public Instant getTimestamp() { return timestamp; }
    public Map<String, String> getFieldErrors() { return fieldErrors; }
	
}
