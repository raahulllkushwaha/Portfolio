package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.dto.ExperienceDto;
import com.portfolio.service.ExperienceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {

    @Autowired
    private ExperienceService experienceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExperienceDto.Response>>> getAllExperiences() {
        return ResponseEntity.ok(ApiResponse.success(experienceService.getAllExperiences()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExperienceDto.Response>> getExperienceById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(experienceService.getExperienceById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExperienceDto.Response>> createExperience(
            @Valid @RequestBody ExperienceDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Experience created", experienceService.createExperience(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExperienceDto.Response>> updateExperience(
            @PathVariable Long id,
            @Valid @RequestBody ExperienceDto.Request request) {
        return ResponseEntity.ok(ApiResponse.success("Experience updated",
                experienceService.updateExperience(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.ok(ApiResponse.success("Experience deleted", null));
    }
}
