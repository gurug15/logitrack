package com.project.logitrack.exceptions;

import java.time.Instant;

public class ErrorMessage {
	public String message;
	public String details;
	public Instant timestamp;
	public ErrorMessage(String message, String details, Instant timestamp) {
		super();
		this.message = message;
		this.details = details;
		this.timestamp = timestamp;
	}
	  // Getters
    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
	
}
