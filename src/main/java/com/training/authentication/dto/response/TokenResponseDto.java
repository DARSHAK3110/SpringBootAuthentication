package com.training.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto  extends CustomBaseResponseDto{
	private String token;	
	private String role;
	private Long userId;
	private String refreshToken;
}
