package com.training.authentication.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.dto.response.ClaimsResponseDto;
import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.entity.User;
import com.training.authentication.entity.enums.Roles;
import com.training.authentication.repository.UserRepository;
import com.training.authentication.security.CustomUserDetail;
import com.training.authentication.security.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	public List<UserResponseDto> getAllUsers() {
		List<User> findAll = this.userRepository.findAllByDeletedAtIsNull();
		List<UserResponseDto> usersDto = new ArrayList<>();
		findAll.forEach(user -> {
			UserResponseDto dto = new UserResponseDto();
			dto.setFirstName(user.getFirstName());
			dto.setLastName(user.getLastName());
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
			dto.setPhoneNumber(user.getPhoneNumber());
			dto.setUserId(user.getUserId());
			dto.setRole(user.getRole());
			return dto;
		}
		return null;
	}

	public String saveUser(UserRequestDto userDto) {

		User user = new User();
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setRole(Roles.valueOf(userDto.getRole()));
		this.userRepository.save(user);
		Map<String,Object> map = new HashedMap<>();
		map.put("name", user.getFirstName()+" "+user.getLastName());
		map.put("role", user.getRole().name());
		return jwtService.generateToken(new CustomUserDetail(user),map);
	}

	@Transactional
	public void deleteUser(Long userId) {
		this.userRepository.deleteUser(userId);
	}

	public String updateUser(Long userId, UserRequestDto user) {
		Optional<User> userOptional = this.userRepository.findByUserIdAndDeletedAtIsNull(userId);
		User savedUser = null;
		if (userOptional.isPresent()) {
			savedUser = userOptional.get();
			savedUser.setFirstName(user.getFirstName());
			savedUser.setLastName(user.getLastName());
			savedUser.setPassword(passwordEncoder.encode(user.getPassword()));
			savedUser.setUpdatedAt(new Date());
			savedUser.setRole(Roles.valueOf(user.getRole()));
			this.userRepository.save(savedUser);
			Map<String,Object> map = new HashedMap<>();
			map.put("name", user.getFirstName()+" "+user.getLastName());
			map.put("role", user.getRole());
			return jwtService.generateToken(new CustomUserDetail(savedUser),map);
		}
		return null;
	}

	public String generateToken(Long phoneNumer) {
		Optional<User> userOptional = this.userRepository.findByPhoneNumberAndDeletedAtIsNull(phoneNumer);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			Map<String,Object> map = new HashedMap<>();
			map.put("name", user.getFirstName()+" "+user.getLastName());
			map.put("role", user.getRole().name());
			return jwtService.generateToken(new CustomUserDetail(user),map);
		}
		return null;
	}

	public ClaimsResponseDto getDetails(String token) {
		token = token.replace("Bearer ", "");
		Claims extractAllClaims = this.jwtService.extractAllClaims(token);
		ClaimsResponseDto res = new ClaimsResponseDto();
		res.setSubject(extractAllClaims.getSubject());
		res.setIssuedDate(extractAllClaims.getIssuedAt());
		res.setExpireDate(extractAllClaims.getExpiration());
		res.setName((String) extractAllClaims.get("name"));
		res.setRole((String) extractAllClaims.get("role"));
		return res;
	}
}
