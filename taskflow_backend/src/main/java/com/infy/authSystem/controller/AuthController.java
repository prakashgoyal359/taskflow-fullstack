package com.infy.authSystem.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.infy.authSystem.dto.LoginDto;
import com.infy.authSystem.dto.RegisterDto;
import com.infy.authSystem.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    // 🔹 REGISTER API
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto){

        authService.register(dto);

        return ResponseEntity.ok(
                Map.of("message", "User Registered Successfully")
        );
    }

    public AuthController(AuthService authService) {
		this.authService = authService;
	}

	// 🔹 LOGIN API (UPDATED)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto){

        // service already returns token + name
        return ResponseEntity.ok(authService.login(dto));
    }
}