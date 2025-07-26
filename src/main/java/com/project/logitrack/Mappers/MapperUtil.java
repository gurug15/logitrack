package com.project.logitrack.Mappers;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.logitrack.Entity.Roles;
import com.project.logitrack.Entity.User;
import com.project.logitrack.dto.UserDto;

public class MapperUtil {

//	 private static final Roles DEFAULT_USER_ROLE_ID = new Roles(1,"User",);
	 
	 
	 public static User toUser(UserDto dto, BCryptPasswordEncoder passwordEncoder) {
	        if (dto == null) {
	            return null;
	        }
	        
	        User user = new User();
	        user.setName(dto.getName());
	        user.setEmail(dto.getEmail());
	        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
	        user.setPhone(dto.getPhone());
//	        user.setRoleId(DEFAULT_USER_ROLE_ID);
	        user.setLogisticCenterId(null); // Will be assigned by admin later
	        user.setCreatedAt(LocalDateTime.now());
	        user.setUpdatedAt(LocalDateTime.now());
	        
	        return user;
	    }
	 
	 
	 
	 public static User toUser(UserDto dto, BCryptPasswordEncoder passwordEncoder, 
             Integer roleId, Integer logisticCenterId) {
				if (dto == null) {
					return null;
				}
			
			User user = new User();
			user.setName(dto.getName());
			user.setEmail(dto.getEmail());
			user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
			user.setPhone(dto.getPhone());
//			user.setRoleId();;
			user.setLogisticCenterId(logisticCenterId);
			user.setCreatedAt(LocalDateTime.now());
			user.setUpdatedAt(LocalDateTime.now());
			
			return user;
			}
			
			
			public static UserDto toUserDto(User user) {
			if (user == null) {
			return null;
			}
			
			UserDto dto = new UserDto();
			dto.setName(user.getName());
			dto.setEmail(user.getEmail());
			dto.setPhone(user.getPhone());
			// Note: password is not included for security reasons
			
			return dto;
			}
			
			public static User updateUserFromDto(User existingUser, UserDto dto, BCryptPasswordEncoder passwordEncoder) {
			if (existingUser == null || dto == null) {
			return existingUser;
			}
			
			if (dto.getName() != null) {
			existingUser.setName(dto.getName());
			}
			if (dto.getEmail() != null) {
			existingUser.setEmail(dto.getEmail());
			}
			if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
			existingUser.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
			}
			if (dto.getPhone() != null) {
			existingUser.setPhone(dto.getPhone());
			}
			
			existingUser.setUpdatedAt(LocalDateTime.now());	
			
			return existingUser;
			}
}
