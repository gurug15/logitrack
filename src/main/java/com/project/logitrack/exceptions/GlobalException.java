package com.project.logitrack.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalException {

	// Handle validation errors for @Valid annotated requests
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        ErrorMessage errorMessage = new ErrorMessage(
            "Validation failed for input fields.",
            fieldErrors,
            Instant.now()
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    // Handle generic unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        System.err.println("UNEXPECTED ERROR: " + ex.getMessage());
        ex.printStackTrace();

        ErrorMessage userFriendlyError = new ErrorMessage(
            "An unexpected server error occurred.",
            "Please contact support if the issue persists.",
            Instant.now()
        );
        return new ResponseEntity<>(userFriendlyError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessage> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        ErrorMessage errorDetails = new ErrorMessage(
            "Conflict",
            ex.getMessage(),
            Instant.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ErrorMessage errormsg = new ErrorMessage(
            "Authentication Failed", 
            ex.getMessage(),
            Instant.now()
        );
        return new ResponseEntity<>(errormsg, HttpStatus.UNAUTHORIZED);
    }
}
