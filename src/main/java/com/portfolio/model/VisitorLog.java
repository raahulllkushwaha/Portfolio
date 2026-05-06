package com.portfolio.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitor_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VisitorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;
    private String userAgent;
    private String page;
    private String country;
    private LocalDateTime visitedAt;

    @PrePersist
    public void prePersist() {
        this.visitedAt = LocalDateTime.now();
    }
}