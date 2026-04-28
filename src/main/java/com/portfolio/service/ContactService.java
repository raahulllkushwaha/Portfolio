package com.portfolio.service;

import com.portfolio.dto.ContactDto;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.ContactMessage;
import com.portfolio.model.ContactMessage.MessageStatus;
import com.portfolio.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.portfolio.owner-email}")
    private String ownerEmail;

    public ContactDto.Response sendMessage(ContactDto.Request request) {
        ContactMessage message = ContactMessage.builder()
                .senderName(request.getSenderName())
                .senderEmail(request.getSenderEmail())
                .subject(request.getSubject())
                .message(request.getMessage())
                .build();

        ContactMessage saved = contactMessageRepository.save(message);

        // Send email notification to portfolio owner (non-blocking fail)
        try {
            sendEmailNotification(saved);
        } catch (Exception e) {
            // Log but don't fail the request if mail is not configured
            System.err.println("Mail notification failed: " + e.getMessage());
        }

        return toResponse(saved);
    }

    public List<ContactDto.Response> getAllMessages() {
        return contactMessageRepository.findAllByOrderByReceivedAtDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ContactDto.Response> getMessagesByStatus(MessageStatus status) {
        return contactMessageRepository.findByStatusOrderByReceivedAtDesc(status)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ContactDto.Response getMessageById(Long id) {
        ContactMessage msg = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        // Auto-mark as READ when admin views it
        if (msg.getStatus() == MessageStatus.UNREAD) {
            msg.setStatus(MessageStatus.READ);
            contactMessageRepository.save(msg);
        }
        return toResponse(msg);
    }

    public ContactDto.Response updateStatus(Long id, MessageStatus status) {
        ContactMessage msg = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        msg.setStatus(status);
        return toResponse(contactMessageRepository.save(msg));
    }

    public void deleteMessage(Long id) {
        if (!contactMessageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Message not found with id: " + id);
        }
        contactMessageRepository.deleteById(id);
    }

    public long getUnreadCount() {
        return contactMessageRepository.countByStatus(MessageStatus.UNREAD);
    }

    // ---- Email Helper ----

    private void sendEmailNotification(ContactMessage msg) {
        if (mailSender == null) return;

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(ownerEmail);
        mail.setSubject("New Portfolio Contact: " + (msg.getSubject() != null ? msg.getSubject() : "No Subject"));
        mail.setText(String.format(
                "You have a new message from your portfolio contact form.\n\n" +
                "Name: %s\nEmail: %s\n\nMessage:\n%s",
                msg.getSenderName(), msg.getSenderEmail(), msg.getMessage()
        ));
        mailSender.send(mail);
    }

    // ---- Mapper ----

    private ContactDto.Response toResponse(ContactMessage m) {
        ContactDto.Response res = new ContactDto.Response();
        res.setId(m.getId());
        res.setSenderName(m.getSenderName());
        res.setSenderEmail(m.getSenderEmail());
        res.setSubject(m.getSubject());
        res.setMessage(m.getMessage());
        res.setStatus(m.getStatus());
        res.setReceivedAt(m.getReceivedAt());
        return res;
    }
}
