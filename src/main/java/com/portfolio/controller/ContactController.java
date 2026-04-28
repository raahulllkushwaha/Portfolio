package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.dto.ContactDto;
import com.portfolio.model.ContactMessage.MessageStatus;
import com.portfolio.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    /** Public endpoint — anyone can send a contact message */
    @PostMapping
    public ResponseEntity<ApiResponse<ContactDto.Response>> sendMessage(
            @Valid @RequestBody ContactDto.Request request) {
        ContactDto.Response saved = contactService.sendMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Message sent successfully", saved));
    }

    /** Admin: list all messages, optionally filter by status */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ContactDto.Response>>> getAllMessages(
            @RequestParam(required = false) MessageStatus status) {
        List<ContactDto.Response> messages = (status != null)
                ? contactService.getMessagesByStatus(status)
                : contactService.getAllMessages();
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    /** Admin: view a single message (auto-marks as READ) */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContactDto.Response>> getMessageById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(contactService.getMessageById(id)));
    }

    /** Admin: update message status */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ContactDto.Response>> updateStatus(
            @PathVariable Long id,
            @RequestBody ContactDto.StatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Status updated",
                contactService.updateStatus(id, request.getStatus())));
    }

    /** Admin: delete a message */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long id) {
        contactService.deleteMessage(id);
        return ResponseEntity.ok(ApiResponse.success("Message deleted", null));
    }

    /** Admin: unread count badge */
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount() {
        return ResponseEntity.ok(ApiResponse.success(contactService.getUnreadCount()));
    }
}
