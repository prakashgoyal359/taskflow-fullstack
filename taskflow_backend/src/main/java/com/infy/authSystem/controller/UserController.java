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

    @GetMapping
    public List<Map<String, Object>> getUsers() {

        return userRepo.findAll().stream().map(u -> {

            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("fullName", u.getFullName());

            return map;

        }).toList();
    }

	public UserController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
}