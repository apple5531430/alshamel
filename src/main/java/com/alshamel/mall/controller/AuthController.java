package com.alshamel.mall.controller;

import com.alshamel.mall.common.ApiResponse;
import com.alshamel.mall.dto.LoginRequest;
import com.alshamel.mall.dto.LoginResponse;
import com.alshamel.mall.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }
}
