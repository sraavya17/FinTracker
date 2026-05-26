package com.finTracker.utility;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finTracker.exception.FinTrackerException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
	
	@Autowired
	private Environment environment;
	
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	//Handles all business logic errors
	@ExceptionHandler(FinTrackerException.class)
	public ResponseEntity<ErrorInfo> FinTrackerExceptionHandler(FinTrackerException exception) {
		String key = exception.getMessage();
		String message = environment.getProperty(key);
		if(message == null) {
			message = key;
		}
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorInfo.setErrorMessage(message);
		
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}
	
	//Handles all bean validation failure errors(@Valid)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorInfo> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
		String message = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorInfo.setErrorMessage(message);
		
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}
	
	//Handles all @Validated constraint violations
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorInfo> ConstraintViolationExceptionHandler(ConstraintViolationException exception) {
		String message = exception.getConstraintViolations().stream().map(c -> c.getMessage()).collect(Collectors.joining(", "));
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorInfo.setErrorMessage(message);
		
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}
	
	//Handles missing fields in request parameters
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorInfo> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException exception) {
		String message = "Please provide a valid "+exception.getParameterName();
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorInfo.setErrorMessage(message);
		
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
		
	}
	
	//Handles unexpected-general errors
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> GeneralExceptionHandler(Exception exception) {
		String message = environment.getProperty("General.EXCEPTION_MESSAGE");
		if(message == null) {
			message = "Unexpected Error occured";
		}
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorInfo.setErrorMessage(message);
		
		return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
