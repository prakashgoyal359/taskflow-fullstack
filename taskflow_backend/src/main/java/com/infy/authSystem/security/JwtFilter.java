package com.infy.authSystem.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.infy.authSystem.entity.User;
import com.infy.authSystem.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepo) {
		super();
		this.jwtUtil = jwtUtil;
		this.userRepo = userRepo;
	}

	@Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {
            	

                // 🔹 Extract email from JWT
                String email = jwtUtil.extractEmail(token);

                if (email != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 🔹 Load user from DB
                    User user = userRepo.findByEmail(email).orElse(null);

                    if (user != null) {

                        // 🔹 Authority WITHOUT ROLE_ prefix
                        SimpleGrantedAuthority authority =
                                new SimpleGrantedAuthority(user.getRole().name());

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        user.getEmail(),
                                        null,
                                        List.of(authority)
                                );

                        auth.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request)
                        );

                        SecurityContextHolder.getContext().setAuthentication(auth);

                        // Debug log
                        System.out.println("Authenticated user: "
                                + user.getEmail() + " Role: "
                                + user.getRole().name());
                        
                        System.out.println("User role from DB: " + user.getRole());
                    }
                }

            } catch (Exception e) {
                System.out.println("JWT Error: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}