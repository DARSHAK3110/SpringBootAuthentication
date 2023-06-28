package com.training.authentication.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.authentication.dto.request.FilterDto;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.dto.response.ClaimsResponseDto;
import com.training.authentication.dto.response.TokenResponseDto;
import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.entity.RefreshToken;
import com.training.authentication.entity.User;
import com.training.authentication.entity.enums.Roles;
import com.training.authentication.repository.RefreshTokenRepository;
import com.training.authentication.repository.UserRepository;
import com.training.authentication.repository.UserSpecifications;
import com.training.authentication.security.CustomUserDetail;
import com.training.authentication.security.JwtService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	@Autowired
	private UserSpecifications userSpecifications;
	
	
	
	public Map<String, Object > getAllUsers(FilterDto searchWord) {
		//Specification<User> userSpec =  Specification.where(UserSpecifications.searchSpecification(searchWord));
		//Page<User> findAll = userRepository.findAll( userSpec,PageRequest.of(searchWord.getPageNumber(),searchWord.getSetSize()));
		return userSpecifications.searchSpecification(searchWord);
//		//Long totalUser = findAll.getTotalElements();
//		
//		List<UserResponseDto> usersDto = new ArrayList<>();
//		findAll.forEach(user -> {
//		
//			UserResponseDto dto = new UserResponseDto();
//			dto.setFirstName(user.getFirstName());
//			dto.setLastName(user.getLastName());
//			dto.setPhoneNumber(user.getPhoneNumber());
//			dto.setUserId(user.getUserId());
//			dto.setRole(user.getRole());
//			usersDto.add(dto);
//		});
//		return null;
	}

	public UserResponseDto getUser(Long userId) {

		Optional<User> userResult = this.userRepository.findByPhoneNumberAndDeletedAtIsNull(userId);
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
			savedUser.setUpdatedAt(new Date());
			this.userRepository.save(savedUser);
			Map<String,Object> map = new HashedMap<>();
			map.put("name", user.getFirstName()+" "+user.getLastName());
			map.put("role", user.getRole());
			return jwtService.generateToken(new CustomUserDetail(savedUser),map);
		}
		return null;
	}

	public TokenResponseDto generateToken(Long phoneNumer) {
		Optional<User> userOptional = this.userRepository.findByPhoneNumberAndDeletedAtIsNull(phoneNumer);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			Map<String,Object> map = new HashedMap<>();
			map.put("name", user.getFirstName()+" "+user.getLastName());
			map.put("role", user.getRole().name());
			TokenResponseDto res = new TokenResponseDto();
			res.setRole(user.getRole().name());
			res.setUserId(user.getPhoneNumber());
			res.setToken(jwtService.generateToken(new CustomUserDetail(user),map));
			res.setRefreshToken(createRefreshToken(phoneNumer).getToken());
			return res;
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
	
	
	public RefreshToken createRefreshToken(Long phoneNumber) {
		
		Optional<User> userOpt = userRepository.findByPhoneNumberAndDeletedAtIsNull(phoneNumber);
		User user =null;
		if(userOpt.isPresent()) {
			user = userOpt.get();
			refreshTokenRepository.deleteAllByUserId(user.getUserId());
		}
		RefreshToken refreshToken = RefreshToken
		.builder()
		.expireAt(new Date((new Date().getTime()+(10*24*60*60*1000))))
		.token(UUID.randomUUID().toString())
		.user(user)
		.build();		
		return refreshTokenRepository.save(refreshToken);
	}

	public TokenResponseDto refreshToken(String token) {
		
		Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);	
		if(refreshTokenOpt.isPresent()) {
		return generateToken(refreshTokenOpt.get().getUser().getPhoneNumber());
		}
		return null;
	
	}
}
