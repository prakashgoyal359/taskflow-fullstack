package com.infy.authSystem.controller;

import com.infy.authSystem.entity.User;
import com.infy.authSystem.entity.UserPreference;
import com.infy.authSystem.repository.UserRepository;
import com.infy.authSystem.repository.UserPreferenceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@RestController
@RequestMapping("/api/users/me")
@CrossOrigin("*")
public class SettingsController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserPreferenceRepository prefRepo;

    // ✅ UPDATE PROFILE
    @PatchMapping("/profile")
    public User updateProfile(@RequestBody Map<String, String> body) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findByEmail(email).orElseThrow();

        // update user
        user.setFullName(body.get("fullName"));
        user.setEmail(body.get("email"));

        userRepo.save(user);

        // update preferences
        UserPreference pref = prefRepo.findById(user.getId())
                .orElse(new UserPreference());

        pref.setUserId(user.getId());
        pref.setAvatarColour(body.get("avatarColour"));
        pref.setBio(body.get("bio"));

        prefRepo.save(pref);

        return user;
    }

    // ✅ GET PROFILE (IMPORTANT for UI load)
    @GetMapping
    public Map<String, Object> getProfile() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findByEmail(email).orElseThrow();

        UserPreference pref = prefRepo.findById(user.getId())
                .orElse(null);

        return Map.of(
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "avatarColour", pref != null ? pref.getAvatarColour() : "#2563EB",
                "bio", pref != null ? pref.getBio() : ""
        );
    }
}