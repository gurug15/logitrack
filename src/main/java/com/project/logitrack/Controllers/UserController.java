package com.project.logitrack.Controllers;



import com.project.logitrack.Entity.User;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.Mappers.UserMapper;
import com.project.logitrack.dto.UpdateUserDto;
import com.project.logitrack.dto.UserAdminDto;
import com.project.logitrack.dto.UserDto;
import com.project.logitrack.dto.UserProfileDto;
import com.project.logitrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET /users?search=
    @GetMapping
    public List<UserAdminDto> getUsers(
        @RequestParam(required = false) String search
    ) {
        // Handle null/empty search parameter
        String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        return userService.getUsersFiltered(cleanSearch);
    }
    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // POST /users
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User createdUser = userService.registerUser(userDto);
        return ResponseEntity.status(201).body(createdUser);
    }

    // PUT /users/{id}
    @PutMapping("/edit/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }
    
    
    @GetMapping("/profile/me")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(@AuthenticationPrincipal UserPrinciple currentUser) {
        // The currentUser object is guaranteed by Spring Security to be the logged-in user.
        // We get the full User entity from it and map to a DTO for a safe response.
        return ResponseEntity.ok(UserMapper.toUserProfileDto(currentUser.getUser()));
    }
    
    @PutMapping("/profile/me")
    public ResponseEntity<UserDto> updateCurrentUserProfile(
            @AuthenticationPrincipal UserPrinciple currentUser,
            @RequestBody UpdateUserDto updateUserDto) {
        
        // Securely get the user's ID from the principal, not from the client.
        Long userId = currentUser.getUser().getId();
        UserDto updatedUser = userService.updateUser(userId, updateUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE /users/{id}
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        boolean deleted = userService.deactivateUser(id);
//        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
//    }
}
