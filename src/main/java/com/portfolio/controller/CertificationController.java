package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.dto.CertificationDto;
import com.portfolio.service.CertificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certifications")
public class CertificationController {

    @Autowired
    private CertificationService certificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CertificationDto.Response>>> getAllCertifications() {
        return ResponseEntity.ok(ApiResponse.success(certificationService.getAllCertifications()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CertificationDto.Response>> getCertificationById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(certificationService.getCertificationById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CertificationDto.Response>> createCertification(
            @Valid @RequestBody CertificationDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Certification created",
                        certificationService.createCertification(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CertificationDto.Response>> updateCertification(
            @PathVariable Long id,
            @Valid @RequestBody CertificationDto.Request request) {
        return ResponseEntity.ok(ApiResponse.success("Certification updated",
                certificationService.updateCertification(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCertification(@PathVariable Long id) {
        certificationService.deleteCertification(id);
        return ResponseEntity.ok(ApiResponse.success("Certification deleted", null));
    }
}
