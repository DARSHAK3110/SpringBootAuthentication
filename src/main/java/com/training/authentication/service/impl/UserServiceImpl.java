package com.training.authentication.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.training.authentication.entity.User;
import com.training.authentication.repository.UserRepository;
import com.training.authentication.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return this.userRepository.findAll();
	}

	@Override
	public User getUser(Long userId) {
		// TODO Auto-generated method stub
		return this.userRepository.findById(userId).get();
	}

	@Override
	public String saveUser(User user) {
		// TODO Auto-generated method stub
		if (user.getFirstName().isEmpty() || user.getFirstName().length() > 16) {
			return "Enter First name and must be atmost 16 character.";
		}
		if (user.getLastName().isEmpty() || user.getLastName().length() > 16) {
			return "Enter Last name and must be atmost 16 character.";
		}
		if (user.getPassword().isEmpty() || user.getPassword().length() > 255) {
			return "Enter Password and must be atmost 255 character.";
		}
		if (user.getPhoneNumber() == null || user.getPhoneNumber().toString().length() != 10) {
			return "Enter Phone number and must be 10 digit.";
		}
		if (user.getRole().isEmpty() || user.getRole().length() > 5) {
			return "Enter role and must be atmost 5 digit.";
		}
		User savedUser = this.userRepository.save(user);
		if (savedUser != null) {
			return "User successfully saved: " + savedUser.getFirstName() + " " + savedUser.getLastName() + "";
		} else {
			return "Something went wrong!!";
		}
	}

	@Override
	public String deleteUser(Long userId) {
		// TODO Auto-generated method stub
		User user = getUser(userId);
		this.userRepository.delete(user);
		return "User successfully deleted: " + user.getFirstName() + " " + user.getLastName() + "";

	}

	@Override
	public String updateUser(Long userId, User user) {
		// TODO Auto-generated method stub
		User savedUser = getUser(userId);
		if (user.getFirstName().isEmpty() || user.getFirstName().length() > 16) {
			return "Enter First name and must be atmost 16 character.";
		}
		savedUser.setFirstName(user.getFirstName());
		if (user.getLastName().isEmpty() || user.getLastName().length() > 16) {
			return "Enter Last name and must be atmost 16 character.";
		}
		savedUser.setLastName(user.getLastName());
		if (user.getPassword().isEmpty() || user.getPassword().length() > 255) {
			return "Enter Password and must be atmost 255 character.";
		}
		savedUser.setPassword(user.getPassword());
		if (user.getPhoneNumber() == null || user.getPhoneNumber().toString().length() != 10) {
			return "Enter Phone number and must be 10 digit.";
		}
		savedUser.setPhoneNumber(user.getPhoneNumber());
		if (user.getRole().isEmpty() || user.getRole().length() > 5) {
			return "Enter role and must be atmost 5 digit.";
		}
		savedUser.setRole(user.getRole());
		savedUser.setUpdatedAt(null);
		User updatedUser = this.userRepository.save(savedUser);
		if (updatedUser != null) {
			return "User successfully updated: " + updatedUser.getFirstName() + " " + updatedUser.getLastName() + "";
		}
		return "Something went wrong!!";
	}

}
