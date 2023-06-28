package com.training.authentication.dto.request;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.authentication.entity.User;
import com.training.authentication.entity.User_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto implements Specification<User> {
	@JsonProperty
	private String firstName;
	@JsonProperty
	private String lastName;
	@JsonProperty
	private String phoneNumber;
	@JsonProperty
	private int pageNumber;
	@JsonProperty
	private int setSize;

	@Override
	public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> pred = new ArrayList<>();
		if (!getFirstName().equals("")) {
			pred.add(criteriaBuilder.like(root.get(User_.FIRST_NAME).as(String.class),
					"%" + getFirstName().toLowerCase() + "%"));

		}
		if (!getLastName().equals("")) {
			pred.add(criteriaBuilder.like(root.get(User_.LAST_NAME).as(String.class),
					"%" + getLastName().toLowerCase() + "%"));

		}
		if (!getPhoneNumber().equals("")) {
			pred.add(criteriaBuilder.like(root.get(User_.PHONE_NUMBER).as(String.class),
					"%" + getPhoneNumber().toLowerCase() + "%"));

		}
		pred.add(criteriaBuilder.isNull(root.get(User_.DELETED_AT)));
		return criteriaBuilder.and(pred.toArray(new Predicate[pred.size()]));
	}

}
