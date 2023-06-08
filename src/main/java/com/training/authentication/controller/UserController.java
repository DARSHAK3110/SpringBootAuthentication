package com.training.authentication.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.training.authentication.entity.User;
import com.training.authentication.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserService userSerivceImpl;
	Logger log = Logger.getLogger(UserController.class.getName());

	@GetMapping("")
	public ResponseEntity<List<User>> getAllUsers() {
		log.info("findAllUsers");
		List<User> users = this.userSerivceImpl.getAllUsers();
		if (users.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.of(Optional.of(users));
	}

	
	@GetMapping("/{userId}")
	public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
		log.info("getUser");
		try {
			User user = this.userSerivceImpl.getUser(userId);
			return ResponseEntity.of(Optional.of(user));				
		}
		catch (NoSuchElementException e) {
			// TODO: handle exception
			log.severe(e.getMessage());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			
		}
		
	}
	
	@PostMapping("")
	public ResponseEntity<String> saveUser(@RequestBody User user){
		try{
			String result = this.userSerivceImpl.saveUser(user);
			return ResponseEntity.of(Optional.of(result));
	}
	catch (Exception e) {
		// TODO: handle exception
		log.severe(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	}
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
		log.info("deleteUser");
		try {
			 String deleteUser = this.userSerivceImpl.deleteUser(userId);
			return ResponseEntity.of(Optional.of(deleteUser));				
		}
		catch (NoSuchElementException e) {
			// TODO: handle exception
			log.severe(e.getMessage());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();	
		}
		catch (Exception e) {
			// TODO: handle exception
			log.severe(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@PutMapping("/{userId}")
	public ResponseEntity<String> updateUser(@RequestBody User user,@PathVariable("userId") Long userId) {
		log.info("updateUser");
		try {
			String updatedUser = this.userSerivceImpl.updateUser(userId,user);
			return ResponseEntity.of(Optional.of(updatedUser));				
		}
		catch (NoSuchElementException e) {
			// TODO: handle exception
			log.severe(e.getMessage());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		catch (Exception e) {
			// TODO: handle exception
			log.severe(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
}

