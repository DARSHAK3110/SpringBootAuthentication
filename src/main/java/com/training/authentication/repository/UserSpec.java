package com.training.authentication.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.training.authentication.dto.request.FilterDto;
import com.training.authentication.entity.User;
import com.training.authentication.entity.User_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserSpec implements Specification<User> {
	
	
	private FilterDto filterDto;

	public UserSpec(FilterDto filterDto) {
		super();
		this.filterDto = filterDto;
	}

	public UserSpec() {
		super();
	}

	@Override
	public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> pred = new ArrayList<>();
		if (!filterDto.getFirstName().equals("")) {
			pred.add(criteriaBuilder.like(root.get(User_.FIRST_NAME).as(String.class),
					"%" + filterDto.getFirstName().toLowerCase() + "%"));
			
		}
		if (!filterDto.getLastName().equals("")) {
			pred.add(criteriaBuilder.like(root.get(User_.LAST_NAME).as(String.class),
					"%" + filterDto.getLastName().toLowerCase() + "%"));
			
		}
		if (!filterDto.getPhoneNumber().equals("")) {
			pred.add(criteriaBuilder.like(root.get(User_.PHONE_NUMBER).as(String.class),
					"%" + filterDto.getPhoneNumber().toLowerCase() + "%"));
			
		}
		pred.add(criteriaBuilder.isNull(root.get(User_.DELETED_AT)));
		return criteriaBuilder.and(pred.toArray(new Predicate[pred.size()]));
	}


}
