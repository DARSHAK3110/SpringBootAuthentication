package com.training.authentication.dto.response;

import java.util.Date;
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
public class ClaimsResponseDto extends CustomBaseResponseDto{
	private String role;
	private String name;
	private String subject;
	private Date issuedDate;
	private Date expireDate;
}
