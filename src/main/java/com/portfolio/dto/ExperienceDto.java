package com.portfolio.dto;

import com.portfolio.model.Experience.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

public class ExperienceDto {

    @Data
    public static class Request {
        @NotBlank private String jobTitle;
        @NotBlank private String company;
        private String companyLogoUrl;
        private String location;
        private EmploymentType employmentType;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean currentJob;
        private String description;
        private int displayOrder;
    }

    @Data
    public static class Response {
        private Long id;
        private String jobTitle;
        private String company;
        private String companyLogoUrl;
        private String location;
        private EmploymentType employmentType;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean currentJob;
        private String description;
        private int displayOrder;
    }
}
