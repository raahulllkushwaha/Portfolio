package com.portfolio.dto;

import com.portfolio.model.Project.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class ProjectDto {

    @Data
    public static class Request {
        @NotBlank private String title;
        private String description;
        private String thumbnailUrl;
        private String liveDemoUrl;
        private String githubUrl;
        private ProjectStatus status;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean featured;
        private int displayOrder;
        private List<String> technologies;
    }

    @Data
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private String thumbnailUrl;
        private String liveDemoUrl;
        private String githubUrl;
        private ProjectStatus status;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean featured;
        private int displayOrder;
        private List<String> technologies;
    }
}
