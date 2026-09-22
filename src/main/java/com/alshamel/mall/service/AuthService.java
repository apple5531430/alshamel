package com.alshamel.mall.service;

import com.alshamel.mall.dto.LoginRequest;
import com.alshamel.mall.dto.LoginResponse;
import com.alshamel.mall.entity.User;
import com.alshamel.mall.mapper.UserMapper;
import com.alshamel.mall.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    public LoginResponse login(LoginRequest request) {
        // 认证失败会抛出 AuthenticationException，由全局异常处理器返回 401
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userMapper.findByUsername(request.getUsername());
        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(token, "Bearer", user.getUsername(), user.getRole(), expirationMs);
    }
}
