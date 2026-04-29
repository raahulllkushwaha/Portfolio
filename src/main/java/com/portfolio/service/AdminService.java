package com.portfolio.service;

import com.portfolio.dto.ChangePasswordDto;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.AdminUser;
import com.portfolio.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void changePassword(String username, ChangePasswordDto.Request request) {
        // 1. Validate new passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match.");
        }

        // 2. Find the admin
        AdminUser admin = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + username));

        // 3. Verify current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        // 4. Prevent reusing the same password
        if (passwordEncoder.matches(request.getNewPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("New password must be different from current password.");
        }

        // 5. Encode and save new password
        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        adminUserRepository.save(admin);
    }
}