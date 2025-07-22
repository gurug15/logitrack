package com.project.logitrack.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
		
	@GetMapping("/login")
	public String getUser() {
		
		return "user logged in";
	}
}
