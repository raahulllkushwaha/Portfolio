package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.dto.ProfileDto;
import com.portfolio.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileDto.Response>> getProfile() {
        return ResponseEntity.ok(ApiResponse.success(profileService.getProfile()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProfileDto.Response>> createProfile(
            @Valid @RequestBody ProfileDto.Request request) {
        ProfileDto.Response created = profileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Profile created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfileDto.Response>> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody ProfileDto.Request request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully",
                profileService.updateProfile(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Profile deleted", null));
    }
}
