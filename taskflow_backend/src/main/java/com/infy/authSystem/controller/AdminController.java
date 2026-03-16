package com.infy.authSystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.infy.authSystem.entity.User;
import com.infy.authSystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final UserRepository userRepo;

    public AdminController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	// GET ALL USERS
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // CHANGE USER ROLE
    @PatchMapping("/users/{id}/role")
    public User updateRole(@PathVariable Long id, @RequestBody User updatedUser) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(updatedUser.getRole());

        return userRepo.save(user);
    }

    // ACTIVATE / DEACTIVATE USER
    @PatchMapping("/users/{id}/status")
    public User updateStatus(@PathVariable Long id, @RequestBody User updatedUser) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(updatedUser.isActive());

        return userRepo.save(user);
    }

    // DELETE USER
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {

        userRepo.deleteById(id);
    }
    
}