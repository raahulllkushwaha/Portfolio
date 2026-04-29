package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.dto.ChangePasswordDto;
import com.portfolio.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * POST /api/admin/change-password
     * Requires: Bearer token (must be logged in as admin)
     * Body: { "currentPassword": "...", "newPassword": "...", "confirmPassword": "..." }
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordDto.Request request) {

        String username = authentication.getName(); // pulled from JWT
        adminService.changePassword(username, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully. Please login again.", null));
    }
}