package com.project.logitrack.exceptions;

import java.sql.Timestamp;

public class ErrorMessage {
	public String message;
	public String details;
	public Timestamp timestamp;
	public ErrorMessage(String message, String details, Timestamp timestamp) {
		super();
		this.message = message;
		this.details = details;
		this.timestamp = timestamp;
	}
	
	
}
