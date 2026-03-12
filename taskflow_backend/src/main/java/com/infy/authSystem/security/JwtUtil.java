package com.infy.authSystem.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.infy.authSystem.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY =
        Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkey".getBytes());

    // 🔹 GENERATE TOKEN WITH USER DATA
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();

        // ✅ ADD THESE CLAIMS
        claims.put("userId", user.getId());
        claims.put("fullName", user.getFullName());
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims)          // ⭐ ADD HERE
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 🔹 EXTRACT EMAIL
    public String extractEmail(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // 🔹 EXTRACT ROLE
    public String extractRole(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("role");
    }

    // 🔹 EXTRACT USER ID
    public Long extractUserId(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return ((Number) claims.get("userId")).longValue();
    }
}