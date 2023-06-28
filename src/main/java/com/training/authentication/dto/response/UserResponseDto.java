package com.training.authentication.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.training.authentication.entity.User;
import com.training.authentication.entity.User_;
import com.training.authentication.entity.enums.Roles;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDto{
	private Long userId;
	private String firstName;
	private String lastName;
	private Long phoneNumber;
	private Roles role;
	private String password;
	public UserResponseDto(String firstName, String lastName, Long phoneNumber, Roles role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}

	
	
	
		
}
