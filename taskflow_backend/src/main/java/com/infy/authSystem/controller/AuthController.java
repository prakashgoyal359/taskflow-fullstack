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
@CrossOrigin(origins = "http://localhost:4200")   // allow Angular
public class AuthController {

    private final AuthService authService;

    // 🔹 REGISTER API
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto){

        // call service
        authService.register(dto);

        // return JSON instead of plain text
        return ResponseEntity.ok(
                Map.of("message", "User Registered Successfully")
        );
    }

    // 🔹 LOGIN API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto){

        // service returns JWT token
        String token = authService.login(dto);

        // return JSON with token
        return ResponseEntity.ok(
                Map.of("token", token)
        );
    }

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

}