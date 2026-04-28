package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.dto.EducationDto;
import com.portfolio.service.EducationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    @Autowired
    private EducationService educationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EducationDto.Response>>> getAllEducation() {
        return ResponseEntity.ok(ApiResponse.success(educationService.getAllEducation()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EducationDto.Response>> getEducationById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(educationService.getEducationById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EducationDto.Response>> createEducation(
            @Valid @RequestBody EducationDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Education created", educationService.createEducation(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EducationDto.Response>> updateEducation(
            @PathVariable Long id,
            @Valid @RequestBody EducationDto.Request request) {
        return ResponseEntity.ok(ApiResponse.success("Education updated",
                educationService.updateEducation(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEducation(@PathVariable Long id) {
        educationService.deleteEducation(id);
        return ResponseEntity.ok(ApiResponse.success("Education deleted", null));
    }
}
