package com.project.logitrack.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.logitrack.Entity.User;
import com.project.logitrack.dto.UserDto;
import com.project.logitrack.exceptions.UserNotFoundException;
import com.project.logitrack.service.UserService;

@RestController
public class AuthenticationController {
	
	@Autowired
	UserService userService;
	
	
	
	@PostMapping("/signup")
	public ResponseEntity<User> addUser(@RequestBody UserDto userDto){
		return new ResponseEntity<User>(userService.registerUser(userDto),HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody UserDto userDto){
		try {
			String token = userService.verify(userDto);
			// On success, return 200 OK with the token
			return ResponseEntity.ok(token);
		} catch (UserNotFoundException e) {
			// On failure, return 401 Unauthorized with the error message
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
	 @GetMapping("/users/role/{id}")
	    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Integer id) {
	        List<User> users = userService.getUsersByRoleId(id);
	        System.out.println(users);
	        return ResponseEntity.ok(users);
	    }
	}

