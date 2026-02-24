package com.infy.authSystem.service;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.infy.authSystem.dto.LoginDto;
import com.infy.authSystem.dto.RegisterDto;
import com.infy.authSystem.entity.User;
import com.infy.authSystem.repository.UserRepository;
import com.infy.authSystem.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	private final JwtUtil jwtUtil;

    // 🔹 REGISTER USER
    public String register(RegisterDto dto) {

        // check if email already exists
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // create new user
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());

        // 🔐 encode password
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // save to DB
        userRepo.save(user);

        return "User Registered Successfully";
    }

    // 🔹 LOGIN USER (UPDATED)
    public Map<String, String> login(LoginDto dto) {

        // find user by email
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        // generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // return BOTH token + name
        return Map.of(
                "token", token,
                "name", user.getFullName()
        );
    }
}