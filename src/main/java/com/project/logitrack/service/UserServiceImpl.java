package com.project.logitrack.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.LogisticCenter;
import com.project.logitrack.Entity.Roles;
import com.project.logitrack.Entity.User;
import com.project.logitrack.Mappers.UserMapper;
import com.project.logitrack.dto.UpdateUserDto;
import com.project.logitrack.dto.UserAdminDto;
import com.project.logitrack.dto.UserDto;
import com.project.logitrack.exceptions.UserNotFoundException;
import com.project.logitrack.repositories.LogisticCenterRepository;
import com.project.logitrack.repositories.RolesRepository;
import com.project.logitrack.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepo;
	
    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private LogisticCenterRepository logisticCenterRepository;
	
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

	@Override
	public User getUserById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
	}

	@Override
	public List<UserAdminDto> getUsersFiltered(String search) {
		  if (search == null || search.trim().isEmpty()) {
		        // Return all users when no search parameter is provided
		        return userRepo.findAll().stream().map(UserMapper::toUserAdminDto).toList();
		    }
		 List<User> users = userRepo.searchUsersGlobal(search.trim());
		 return users.stream().map(UserMapper::toUserAdminDto).toList();
	}
	
	 @Override
	    public UserDto updateUser(Long id, UpdateUserDto userDto) {
	        Optional<User> userOpt = userRepo.findById(id);
	        if (userOpt.isEmpty()) {
	            return null;
	        }
	        User user = userOpt.get();

	        if (userDto.getName() != null) user.setName(userDto.getName());
	        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
	        if (userDto.getPhone() != null) user.setPhone(userDto.getPhone());

	        if (userDto.getRoleId() != null) {
	            Roles role = rolesRepository.findById(userDto.getRoleId())
	                .orElseThrow(() -> new RuntimeException("Role not found"));
	            user.setRoleId(role);
	        }

	        if (userDto.getLogisticCenterId() != null) {
	            LogisticCenter center = logisticCenterRepository.findById(userDto.getLogisticCenterId())
	                .orElse(null);
	            user.setLogisticCenterId(center);
	        }

	        User updatedUser = userRepo.save(user);
	        return UserMapper.toUserDto(updatedUser);
	    }

//	    @Override
//	    public boolean deactivateUser(Long id) {
//	        Optional<User> userOpt = userRepo.findById(id);
//	        if (userOpt.isEmpty()) {
//	            return false;
//	        }
//	        User user = userOpt.get();
//	        // Implement soft delete; make sure there's a boolean field like `isActive`
//	        user.setIsActive(false); // If your User entity uses this field
//	        userRepo.save(user);
//	        return true;
//	    }

	

}
