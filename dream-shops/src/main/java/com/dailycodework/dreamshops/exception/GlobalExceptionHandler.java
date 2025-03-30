package com.dailycodework.dreamshops.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.dao.DataIntegrityViolationException;
import  static org.springframework.http.HttpStatus.CONFLICT;
import  static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ResponseEntity<String> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException e) {
	    return ResponseEntity.status(CONFLICT)
	                         .body("An optimistic locking conflict occurred. Please retry your request.");
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> violationResponse (DataIntegrityViolationException ex) throws Exception {
		return ResponseEntity.status(CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex){
			String message="You do not have permission to this action";
			return  new ResponseEntity<>(message,FORBIDDEN);
	}
}
