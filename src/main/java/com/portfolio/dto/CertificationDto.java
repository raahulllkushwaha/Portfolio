package com.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

public class CertificationDto {

    @Data
    public static class Request {
        @NotBlank private String name;
        @NotBlank private String issuingOrganization;
        private String credentialId;
        private String credentialUrl;
        private LocalDate issueDate;
        private LocalDate expiryDate;
        private String badgeImageUrl;
        private int displayOrder;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private String issuingOrganization;
        private String credentialId;
        private String credentialUrl;
        private LocalDate issueDate;
        private LocalDate expiryDate;
        private String badgeImageUrl;
        private int displayOrder;
    }
}
