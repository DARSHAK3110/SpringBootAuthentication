package com.training.authentication.response;

public class CustomBaseResponse {
	
	protected String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public CustomBaseResponse(String message) {
		super();
		this.message = message;
	}
	public CustomBaseResponse() {
		super();
	}
	
}
