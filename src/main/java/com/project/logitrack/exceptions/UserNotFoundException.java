package com.project.logitrack.exceptions;

public class UserNotFoundException extends RuntimeException {
	// Constructor that accepts a message
    public UserNotFoundException(String message) {
        super(message);
    }

    // Optional: default constructor
    public UserNotFoundException() {
        super("User not found");
    }

    // Optional: constructor with cause (other throwable)
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
