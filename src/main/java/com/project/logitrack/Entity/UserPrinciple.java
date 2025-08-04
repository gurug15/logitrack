package com.project.logitrack.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrinciple implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3949336193741703686L;
	
	private User user;

	
	public UserPrinciple(User user) {
		this.user = user;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        // Check if the user object and its role are not null
		if (user != null && user.getRoleId() != null) {
            // Create a single authority based on the roleName from the database
            // (e.g., "ROLE_ADMIN", "ROLE_SUBADMIN")
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRoleId().getRoleName());
			return List.of(authority);
		}
		// If no role is found for any reason, return an empty list of authorities
		return Collections.emptyList();
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPasswordHash();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}
	
	 public User getUser() {
	        return this.user;
	    }
}
