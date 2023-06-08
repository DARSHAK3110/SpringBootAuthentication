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
	public List<User> getAllUsers(){
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
		 User savedUser = this.userRepository.save(user);
		 if(savedUser != null) {
			 return "User successfully saved: "+savedUser.getFirstName()+" "+savedUser.getLastName()+"";
		 }
		 return "Something went wrong!!";
	}


	@Override
	public String deleteUser(Long userId) {
		// TODO Auto-generated method stub
		User user = getUser(userId);
		this.userRepository.delete(user);
		 return "User successfully deleted: "+user.getFirstName()+" "+user.getLastName()+"";
		 
	}


	@Override
	public String updateUser(Long userId, User user) {
		// TODO Auto-generated method stub
		User savedUser =  getUser(userId);
		savedUser.setFirstName(user.getFirstName());
		savedUser.setLastName(user.getLastName());
		savedUser.setPassword(user.getPassword());
		savedUser.setPhoneNumber(user.getPhoneNumber());
		savedUser.setRole(user.getRole());
		savedUser.setUpdatedAt(null);
		User updatedUser = this.userRepository.save(savedUser);
		 if(updatedUser != null) {
			 return "User successfully updated: "+updatedUser.getFirstName()+" "+updatedUser.getLastName()+"";
		 }
		 return "Something went wrong!!";
	}

	
}
