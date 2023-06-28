package com.training.authentication.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.authentication.dto.request.FilterDto;
import com.training.authentication.dto.request.UserLoginRequestDto;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.dto.response.ClaimsResponseDto;
import com.training.authentication.dto.response.CustomBaseResponseDto;
import com.training.authentication.dto.response.TokenResponseDto;
import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.service.UserService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
@PropertySource("classpath:message.properties")
@RequestMapping("/api/v1/users")
public class UserController {
	
	private static final String OPERATION_SUCCESS = "operation.success";
	private final Environment env;
	private final UserService userSerivceImpl;
	Logger log = Logger.getLogger(UserController.class.getName());
	private final AuthenticationManager authenticationManager;
 
	@GetMapping //get all user
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Map<String,Object>> getAllUsers(@RequestParam(value = "searchWord") String searchWord) throws IOException {
		FilterDto dto = new ObjectMapper().readValue(searchWord.getBytes(), FilterDto.class);
		Map<String, Object> allUsers = this.userSerivceImpl.getAllUsers(dto);
		return ResponseEntity.of(Optional.of(allUsers));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal")
	@GetMapping("/{userId}") //get user
	public ResponseEntity<UserResponseDto> getUser(@PathVariable("userId") @Min(1) String userId) {
		UserResponseDto user = this.userSerivceImpl.getUser(Long.parseLong(userId));
		return ResponseEntity.of(Optional.of(user));
	}

	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{userId}") //delete user
	public ResponseEntity<CustomBaseResponseDto> deleteUser(@PathVariable("userId") @Min(1) Long userId) {
		this.userSerivceImpl.deleteUser(userId);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal")
	@PutMapping("/{userId}") //update user
	public ResponseEntity<CustomBaseResponseDto> updateUser(@Valid @RequestBody UserRequestDto user,
			@PathVariable("userId") @Min(1) Long userId) {
		this.userSerivceImpl.updateUser(userId, user);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
	
	
	@PostMapping //ad user and get token
	public ResponseEntity<TokenResponseDto> saveUser(@Valid @RequestBody UserRequestDto user) {
		this.userSerivceImpl.saveUser(user);
		TokenResponseDto res = new TokenResponseDto();
		res.setMessage(env.getRequiredProperty(OPERATION_SUCCESS));
		return ResponseEntity.ok(res);
	}

	
	@PostMapping("/login") //get refresh token
	public ResponseEntity<TokenResponseDto> loginUser(@Valid @RequestBody UserLoginRequestDto user) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), user.getPassword()));
		TokenResponseDto res = this.userSerivceImpl.generateToken(user.getPhoneNumber());
		res.setMessage(env.getRequiredProperty(OPERATION_SUCCESS));
		return ResponseEntity.ok(res);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/details") //get details of token
	ResponseEntity<ClaimsResponseDto> tokenDetails(@NonNull HttpServletRequest request ){
		String header = request.getHeader("Authorization");
		ClaimsResponseDto details = this.userSerivceImpl.getDetails(header);
		details.setMessage(env.getRequiredProperty(OPERATION_SUCCESS));
		return ResponseEntity.ok(details);
	}
	@PostMapping("/refresh") //get refresh token
	public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody String token) {
		TokenResponseDto res =  this.userSerivceImpl.refreshToken(token);
		res.setMessage(env.getRequiredProperty(OPERATION_SUCCESS));
		return ResponseEntity.ok(res);
		
	}
}
