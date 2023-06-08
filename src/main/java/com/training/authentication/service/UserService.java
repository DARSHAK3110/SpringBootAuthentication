package com.training.authentication.service;

import java.util.List;

import com.training.authentication.entity.User;

public interface UserService {

	List<User> getAllUsers();

	User getUser(Long userId);

	String saveUser(User user);

	String deleteUser(Long userId);

	String updateUser(Long userId, User user);

	
}
