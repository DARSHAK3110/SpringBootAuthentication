package com.training.authentication.dto.response;

import com.training.authentication.entity.enums.Roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDto {
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
