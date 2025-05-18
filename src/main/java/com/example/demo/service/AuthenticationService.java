package com.example.demo.service;

import com.example.demo.JwtUtils;
import com.example.demo.dto.JwtResponseDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private MyUserDetailsService myUserDetailsService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;

    public JwtResponseDto register(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user =
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(rawPassword))
                        .build();
        userRepository.save(user);

        // Login
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        return this.authenticateAndGenerateToken(auth);
    }

    public JwtResponseDto login(String username, String password) {
        UsernamePasswordAuthenticationToken unauthorizedToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(unauthorizedToken);
        return this.authenticateAndGenerateToken(auth);
    }

    public JwtResponseDto authenticateAndGenerateToken(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());
        String username = authentication.getName();
        return new JwtResponseDto(jwt, username);
    }
}
