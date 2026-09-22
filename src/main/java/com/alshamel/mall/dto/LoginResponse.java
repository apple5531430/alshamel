package com.alshamel.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType;
    private String username;
    private String role;
    private long expiresInMs;
}
