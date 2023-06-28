package com.training.authentication.repository;

import java.util.Map;

public class SearchCriteria {
	Map<String, String> rest;
	
	public Map<String, String> getRest() {
		return rest;
	}

	public void setRest(Map<String, String> rest) {
		this.rest = rest;
	}

	public SearchCriteria(Map<String, String> rest) {
		super();
		this.rest = rest;
	}

	public SearchCriteria() {
		super();
	}
	
	
}
