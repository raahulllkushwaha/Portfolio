package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.dto.AuthDto;
import com.portfolio.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDto.LoginResponse>> login(
            @Valid @RequestBody AuthDto.LoginRequest request) {
        AuthDto.LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    /**
     * POST /api/auth/register-admin
     * One-time setup endpoint. Disable or delete after first use in production.
     */
    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<String>> registerAdmin(
            @RequestParam String username,
            @RequestParam String password) {
        String msg = authService.registerAdmin(username, password);
        return ResponseEntity.ok(ApiResponse.success(msg, null));
    }
}
