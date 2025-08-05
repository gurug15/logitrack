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
	public ResponseEntity<ErrorMessage> parentException(Exception ex){
		
		ErrorMessage errormsg = new ErrorMessage(ex.getMessage(),ex.getStackTrace().toString(), Instant.now());
		
		return new ResponseEntity<ErrorMessage>(errormsg,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
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
