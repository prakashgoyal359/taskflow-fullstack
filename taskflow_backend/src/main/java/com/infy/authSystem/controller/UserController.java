package com.infy.authSystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.infy.authSystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@GetMapping
    public List<Map<String, Object>> getUsers() {

        return userRepo.findAll().stream().map(u -> {

            Map<String, Object> map = new HashMap<>();

            map.put("id", u.getId());
            map.put("fullName", u.getFullName());
            map.put("email", u.getEmail());
            map.put("role", u.getRole().name());

            return map;

        }).toList();
    }
}