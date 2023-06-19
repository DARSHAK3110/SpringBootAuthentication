package com.training.authentication.controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RestController;
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
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		List<UserResponseDto> users = this.userSerivceImpl.getAllUsers();
		return ResponseEntity.of(Optional.of(users));
	}

	@GetMapping("/{userId}") //get user
	public ResponseEntity<UserResponseDto> getUser(@PathVariable("userId") @Min(1) Long userId) {
		UserResponseDto user = this.userSerivceImpl.getUser(userId);
		return ResponseEntity.of(Optional.of(user));
	}

	

	@DeleteMapping("/{userId}") //delete user
	public ResponseEntity<CustomBaseResponseDto> deleteUser(@PathVariable("userId") @Min(1) Long userId) {
		this.userSerivceImpl.deleteUser(userId);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@PutMapping("/{userId}") //update user
	public ResponseEntity<CustomBaseResponseDto> updateUser(@Valid @RequestBody UserRequestDto user,
			@PathVariable("userId") @Min(1) Long userId) {
		this.userSerivceImpl.updateUser(userId, user);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
	
	
	@PostMapping //ad user and get token
	public ResponseEntity<TokenResponseDto> saveUser(@Valid @RequestBody UserRequestDto user) {
		String token = this.userSerivceImpl.saveUser(user);
		TokenResponseDto res = new TokenResponseDto();
		res.setMessage(env.getRequiredProperty(OPERATION_SUCCESS));
		res.setToken(token);
		return ResponseEntity.ok(res);
	}

	
	@PostMapping("/login") //get refresh token
	public ResponseEntity<TokenResponseDto> loginUser(@Valid @RequestBody UserLoginRequestDto user) {
		System.out.println(user);
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), user.getPassword()));
		String userRes = this.userSerivceImpl.generateToken(user.getPhoneNumber());
		TokenResponseDto res = new TokenResponseDto();
		res.setMessage(env.getRequiredProperty(OPERATION_SUCCESS));
		res.setToken(userRes);
		return ResponseEntity.ok(res);
	}
	
	@GetMapping("/details") //get details of token
	ResponseEntity<ClaimsResponseDto> tokenDetails(@NonNull HttpServletRequest request ){
		String header = request.getHeader("Authorization");
		ClaimsResponseDto details = this.userSerivceImpl.getDetails(header);
		details.setMessage(env.getRequiredProperty(OPERATION_SUCCESS));
		return ResponseEntity.ok(details);
	}

}
