package com.portfolio.service;

import com.portfolio.dto.AuthDto;
import com.portfolio.model.AdminUser;
import com.portfolio.repository.AdminUserRepository;
import com.portfolio.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);
        return new AuthDto.LoginResponse(token, request.getUsername());
    }

    /**
     * One-time admin registration. Fails if any admin already exists.
     * In production, call this once then remove or gate behind environment variable.
     */
    public String registerAdmin(String username, String password) {
        if (adminUserRepository.existsByUsername(username)) {
            throw new IllegalStateException("Admin with username '" + username + "' already exists.");
        }
        AdminUser admin = AdminUser.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        adminUserRepository.save(admin);
        return "Admin registered successfully.";
    }
}
