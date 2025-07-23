package com.project.logitrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.User;
import com.project.logitrack.Mappers.MapperUtil;
import com.project.logitrack.dto.UserDto;
import com.project.logitrack.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepo;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepo.findAll() ;
	}

	@Override
	public User registerUser(UserDto userDto) {
		
		User user = MapperUtil.toUser(userDto, encoder);
		user = userRepo.save(user);
		return user;
	}

	

}
