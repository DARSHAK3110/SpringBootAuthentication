package com.training.authentication.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ValidationExceptionResponse extends ExceptionResponse {

	private String fieldName;

	@Override
	@JsonIgnore
	public Boolean getIsError() {
		return super.getIsError();
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
