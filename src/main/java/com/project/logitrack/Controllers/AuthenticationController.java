package com.project.logitrack.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.logitrack.Entity.User;
import com.project.logitrack.dto.UserDto;
import com.project.logitrack.service.UserService;

@RestController
public class AuthenticationController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/getOne")
	public String getUser(@RequestParam("id")Long id) {
		
		return "user logged in";
	}
	
	@GetMapping("/getall")
	public List<User> getall(){
		return userService.getAllUsers();
	}
	
	@PostMapping("/signup")
	public ResponseEntity<User> addUser(@RequestBody UserDto userDto){
		return new ResponseEntity<User>(userService.registerUser(userDto),HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody UserDto userDto){
		
		String message = userService.verify(userDto);
		
		return new ResponseEntity<String>(message,HttpStatus.ACCEPTED);
	}
}
