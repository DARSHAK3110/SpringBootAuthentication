package com.training.authentication.exception;


import java.util.NoSuchElementException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.training.authentication.response.ExceptionResponse;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler({NullPointerException.class,NoSuchElementException.class})
	public final ResponseEntity<ExceptionResponse> noSuchElementExceptionHandler(Exception e) {
		
		ExceptionResponse res = new ExceptionResponse("Data not found!!",e);
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<ExceptionResponse> dataIntegrityExceptionHandler(Exception e) {
		
		ExceptionResponse res = new ExceptionResponse("Data already available!!",e);
		return new ResponseEntity<>(res,HttpStatus.CONFLICT);
	}
	

}
