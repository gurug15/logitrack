package com.project.logitrack.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalException {

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
		// For any unexpected exception, log the full error for developers to see
        System.err.println("UNEXPECTED ERROR: " + ex.getMessage());
        ex.printStackTrace();

        // But send a generic, safe, and user-friendly message to the frontend
		ErrorMessage userFriendlyError = new ErrorMessage(
            "An unexpected server error occurred.", // User-friendly message
            "Please contact support if the issue persists.", // User-friendly detail
            Instant.now()
        );
		
		return new ResponseEntity<>(userFriendlyError, HttpStatus.INTERNAL_SERVER_ERROR); // Return a 500 status
	}

	
	@ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessage> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        // This handler will catch business logic errors, like trying to update a completed shipment.
        ErrorMessage errorDetails = new ErrorMessage(
            "Conflict", // A clear title for the error type
            ex.getMessage(), // The specific message from your service (e.g., "Shipment is already out for delivery.")
            Instant.now()
        );
        // Return a 409 Conflict status, which is appropriate for this kind of error.
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
		ErrorMessage errormsg = new ErrorMessage(
            "Authentication Failed", 
            ex.getMessage(), // This will contain "Bad credentials"
            Instant.now()
        );
		return new ResponseEntity<>(errormsg, HttpStatus.UNAUTHORIZED); // Returns 401
	}
}
