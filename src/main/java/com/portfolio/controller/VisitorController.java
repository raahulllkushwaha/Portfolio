package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.service.VisitorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    // Public — called when someone visits the site
    @PostMapping("/track")
    public ResponseEntity<ApiResponse<Void>> track(
            @RequestParam(defaultValue = "/") String page,
            HttpServletRequest request) {
        visitorService.trackVisit(request, page);
        return ResponseEntity.ok(ApiResponse.success("Tracked", null));
    }

    // Admin only — get stats
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(visitorService.getStats()));
    }
}