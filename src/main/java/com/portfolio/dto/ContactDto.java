package com.portfolio.dto;

import com.portfolio.model.ContactMessage.MessageStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

public class ContactDto {

    @Data
    public static class Request {
        @NotBlank private String senderName;
        @Email @NotBlank private String senderEmail;
        private String subject;
        @NotBlank private String message;
    }

    @Data
    public static class Response {
        private Long id;
        private String senderName;
        private String senderEmail;
        private String subject;
        private String message;
        private MessageStatus status;
        private LocalDateTime receivedAt;
    }

    @Data
    public static class StatusUpdateRequest {
        private MessageStatus status;
    }
}
