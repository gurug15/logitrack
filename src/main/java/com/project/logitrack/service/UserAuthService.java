package com.project.logitrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.User;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.repositories.UserRepository;

@Service
public class UserAuthService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user =  userRepo.findByEmail(username);
		
		if(user == null) {
			System.out.println("user not found");
			throw new UsernameNotFoundException("User Not Found: "+ username);
		}
		
		return new UserPrinciple(user);
	}

}
