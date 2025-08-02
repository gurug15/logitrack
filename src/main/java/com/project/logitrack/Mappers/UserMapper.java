package com.project.logitrack.Mappers;

import java.time.OffsetDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.logitrack.Entity.LogisticCenter;
import com.project.logitrack.Entity.Roles;
import com.project.logitrack.Entity.User;
import com.project.logitrack.dto.UpdateUserDto;
import com.project.logitrack.dto.UserAdminDto;
import com.project.logitrack.dto.UserDto;
import com.project.logitrack.repositories.LogisticCenterRepository;
import com.project.logitrack.repositories.RolesRepository;

public class UserMapper {

	public static User toUser(UserDto dto, BCryptPasswordEncoder passwordEncoder) {
		if (dto == null) {
			return null;
		}

		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
		user.setPhone(dto.getPhone());
		user.setLogisticCenterId(null); // Will be assigned by admin later
		user.setCreatedAt(OffsetDateTime.now());
		user.setUpdatedAt(OffsetDateTime.now());

		return user;
	}



	public static User toUser(UserDto dto, BCryptPasswordEncoder passwordEncoder, LogisticCenter logisticCenter) {
		if (dto == null) {
			return null;
		}

		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
		user.setPhone(dto.getPhone());
		user.setLogisticCenterId(logisticCenter);
		user.setCreatedAt(OffsetDateTime.now());
		user.setUpdatedAt(OffsetDateTime.now());

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
	
	public static UserAdminDto toUserAdminDto(User user) {
		if (user == null) {
			return null;
		}

		UserAdminDto dto = new UserAdminDto(
				user.getId(),
		        user.getName(),
		        user.getEmail(),
		        user.getRoleId() != null ? user.getRoleId().getRoleName() : null,
		        user.getLogisticCenterId() != null ? user.getLogisticCenterId().getName() : "N/A"
		    );
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

		existingUser.setUpdatedAt(OffsetDateTime.now());	

		return existingUser;
	}

	public static void updateUserFromDto(User existingUser, UpdateUserDto dto,RolesRepository rolesRepo,LogisticCenterRepository centerRepo ) {
		if (dto.getName() != null) {
			existingUser.setName(dto.getName());
		}
		if (dto.getEmail() != null) {
			existingUser.setEmail(dto.getEmail());
		}
		if (dto.getPhone() != null) {
			existingUser.setPhone(dto.getPhone());
		}
		if (dto.getRoleId() != null) {
			// Fetch the role entity from the repository for referential integrity
			Roles newRole = rolesRepo.findById(dto.getRoleId())
					.orElseThrow(() -> new RuntimeException("Role not found"));
			existingUser.setRoleId(newRole);
		}
		if (dto.getLogisticCenterId() != null) {
			LogisticCenter center = centerRepo.findById(dto.getLogisticCenterId())
					.orElse(null); // Or throw if you require existence
			existingUser.setLogisticCenterId(center);
		}
	}
}
