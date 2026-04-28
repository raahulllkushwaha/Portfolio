package com.portfolio.repository;

import com.portfolio.model.ContactMessage;
import com.portfolio.model.ContactMessage.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findByStatusOrderByReceivedAtDesc(MessageStatus status);
    List<ContactMessage> findAllByOrderByReceivedAtDesc();
    long countByStatus(MessageStatus status);
}
