package com.project.logitrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.User;
import com.project.logitrack.Mappers.UserMapper;
import com.project.logitrack.dto.UserDto;
import com.project.logitrack.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepo;
	
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	AuthenticationManager authManager;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepo.findAll() ;
	}

	@Override
	public User registerUser(UserDto userDto) {
		
		User user = UserMapper.toUser(userDto, encoder);
		user = userRepo.save(user);
		return user;
	}

	@Override
	public String verify(UserDto userDto) {
		Authentication authentication  = authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
		
		if(authentication.isAuthenticated())
			return jwtService.generateToken(userDto.getEmail());
		return "Failure";
	}

	@Override
	public List<User> getUsersByRoleId(Integer roleId) {
	    List<User> users = userRepo.getUserByRoleId(roleId);
        return users;
    }


	

}
