package com.portfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "certifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String issuingOrganization;

    private String credentialId;

    private String credentialUrl;

    private LocalDate issueDate;

    private LocalDate expiryDate;   // null means "No Expiry"

    private String badgeImageUrl;

    private int displayOrder;
}
