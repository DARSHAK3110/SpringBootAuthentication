package com.training.authentication.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ExceptionResponse extends CustomBaseResponse{

	private Boolean isError = true;
	@JsonIgnore
	private Exception exception;
	@JsonIgnore
	private String log;
	
	public Exception getException() {
		return exception;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}

	public Boolean getIsError() {
		return isError;
	}
	public void setIsError(Boolean isError) {
		this.isError = isError;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	public ExceptionResponse(String message, Boolean isError, Exception exception) {
		super();
		this.message = message;
		this.isError = isError;
		this.exception = exception;
	}
	public ExceptionResponse(String message, Boolean isError) {
		super();
		this.message = message;
		this.isError = isError;
	}
	public ExceptionResponse(String message, Exception exception) {
		super();
		this.message = message;
		this.exception = exception;
	}
	
	public ExceptionResponse(String message) {
		super();
		this.message = message;
	}
	public ExceptionResponse() {
		super();
	}
	
	
	
	

}
