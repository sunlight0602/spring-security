package com.example.demo.controller;

import com.example.demo.dto.JwtResponseDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.MessageResponseDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.service.AuthenticationService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDto> register(@RequestBody RegisterRequestDto request) {
        try {
            authenticationService.register(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new MessageResponseDto("註冊成功"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDto("註冊失敗QQ"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        JwtResponseDto responseDto =
                authenticationService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(responseDto);
    }
}
