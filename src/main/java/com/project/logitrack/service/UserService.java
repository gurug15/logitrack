package com.project.logitrack.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.User;
import com.project.logitrack.dto.UserDto;


//@Service
public interface UserService {
	public List<User> getAllUsers();
	public User registerUser(UserDto userDto);
	public String verify(UserDto userDto);
}
