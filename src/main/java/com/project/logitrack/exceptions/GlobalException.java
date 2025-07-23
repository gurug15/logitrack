package com.project.logitrack.exceptions;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> parentException(Exception ex){
		
		ErrorMessage errormsg = new ErrorMessage(ex.getMessage(),ex.getStackTrace().toString(),new Timestamp(System.currentTimeMillis()));
		
		return new ResponseEntity<ErrorMessage>(errormsg,HttpStatus.BAD_REQUEST);
	}
}
