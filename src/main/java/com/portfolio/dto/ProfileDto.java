package com.portfolio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class ProfileDto {

    @Data
    public static class Request {
        @NotBlank private String fullName;
        @NotBlank private String title;
        private String bio;
        @Email private String email;
        private String phone;
        private String location;
        private String profileImageUrl;
        private String resumeUrl;
        private String githubUrl;
        private String linkedinUrl;
        private String twitterUrl;
        private String websiteUrl;
    }

    @Data
    public static class Response {
        private Long id;
        private String fullName;
        private String title;
        private String bio;
        private String email;
        private String phone;
        private String location;
        private String profileImageUrl;
        private String resumeUrl;
        private String githubUrl;
        private String linkedinUrl;
        private String twitterUrl;
        private String websiteUrl;
    }
}
