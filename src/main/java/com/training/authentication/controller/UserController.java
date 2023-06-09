package com.training.authentication.controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.response.CustomBaseResponse;
import com.training.authentication.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@PropertySource("classpath:message.properties")
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private Environment env;
	
	@Autowired
	private UserService userSerivceImpl;
	Logger log = Logger.getLogger(UserController.class.getName());

	@GetMapping
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		List<UserResponseDto> users = this.userSerivceImpl.getAllUsers();
		return ResponseEntity.of(Optional.of(users));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDto> getUser(@PathVariable("userId") @Min(1) Long userId) {
		UserResponseDto user = this.userSerivceImpl.getUser(userId);
		return ResponseEntity.of(Optional.of(user));
	}

	@PostMapping
	public ResponseEntity<CustomBaseResponse> saveUser(@Valid @RequestBody UserRequestDto user) {
		this.userSerivceImpl.saveUser(user);
		return ResponseEntity.ok(new CustomBaseResponse(env.getRequiredProperty("operation.success")));
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<CustomBaseResponse> deleteUser(@PathVariable("userId") @Min(1) Long userId) {
		this.userSerivceImpl.deleteUser(userId);
		return ResponseEntity.ok(new CustomBaseResponse(env.getRequiredProperty("operation.success")));
	}

	@PutMapping("/{userId}")
	public ResponseEntity<CustomBaseResponse> updateUser(@Valid @RequestBody UserRequestDto user,
			@PathVariable("userId") @Min(1) Long userId) {
		this.userSerivceImpl.updateUser(userId, user);
		return ResponseEntity.ok(new CustomBaseResponse(env.getRequiredProperty("operation.success")));
	}
	
}
