package com.training.authentication.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.entity.User;
import com.training.authentication.entity.enums.Roles;
import com.training.authentication.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<UserResponseDto> getAllUsers() {
		List<User> findAll = this.userRepository.findAllByDeletedAtIsNull();
		List<UserResponseDto> usersDto = new ArrayList<>();
		findAll.forEach(user -> {
			UserResponseDto dto = new UserResponseDto();
			dto.setFirstName(user.getFirstName());
			dto.setLastName(user.getLastName());
			dto.setPassword(user.getPassword());
			dto.setPhoneNumber(user.getPhoneNumber());
			dto.setUserId(user.getUserId());
			dto.setRole(user.getRole());
			usersDto.add(dto);
		});
		return usersDto;
	}

	public UserResponseDto getUser(Long userId) {

		Optional<User> userResult = this.userRepository.findByUserIdAndDeletedAtIsNull(userId);
		UserResponseDto dto = new UserResponseDto();
		if (userResult.isPresent()) {
			User user = userResult.get();
			dto.setFirstName(user.getFirstName());
			dto.setLastName(user.getLastName());
			dto.setPassword(user.getPassword());
			dto.setPhoneNumber(user.getPhoneNumber());
			dto.setUserId(user.getUserId());
			dto.setRole(user.getRole());
			return dto;
		}
		return null;
	}

	public void saveUser(UserRequestDto userDto) {

		User user = new User();
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setPassword(userDto.getPassword());
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setRole(Roles.valueOf(userDto.getRole()));
		User savedUser = this.userRepository.save(user);
	}

	@Transactional
	public void deleteUser(Long userId) {
		this.userRepository.deleteUser(userId);
	}

	public void updateUser(Long userId, UserRequestDto user) {
		Optional<User> userOptional = this.userRepository.findByUserIdAndDeletedAtIsNull(userId);
		User savedUser = null;
		if (userOptional.isPresent()) {
			savedUser = userOptional.get();
			savedUser.setFirstName(user.getFirstName());
			savedUser.setLastName(user.getLastName());
			savedUser.setPassword(user.getPassword());
			savedUser.setUpdatedAt(new Date());
			savedUser.setRole(Roles.valueOf(user.getRole()));
			this.userRepository.save(savedUser);
		}
	}
}
