package com.example.demo.dto;

import lombok.Data;

@Data
public class JwtResponseDto {
    private String token;
    private String type = "Bearer"; // 一般用 Bearer token
    private String username;

    public JwtResponseDto(String token, String username) {
        this.token = token;
        this.username = username;
    }
}
