package com.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

public class EducationDto {

    @Data
    public static class Request {
        @NotBlank private String degree;
        @NotBlank private String institution;
        private String institutionLogoUrl;
        private String location;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean currentlyStudying;
        private String grade;
        private String description;
        private int displayOrder;
    }

    @Data
    public static class Response {
        private Long id;
        private String degree;
        private String institution;
        private String institutionLogoUrl;
        private String location;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean currentlyStudying;
        private String grade;
        private String description;
        private int displayOrder;
    }
}
