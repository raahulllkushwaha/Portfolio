package com.portfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "experiences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String jobTitle;

    @NotBlank
    private String company;

    private String companyLogoUrl;

    private String location;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;  // FULL_TIME, PART_TIME, FREELANCE, INTERNSHIP

    private LocalDate startDate;

    private LocalDate endDate;    // null means "Present"

    private boolean currentJob;

    @Column(columnDefinition = "TEXT")
    private String description;  // Responsibilities / achievements

    private int displayOrder;

    public enum EmploymentType {
        FULL_TIME, PART_TIME, FREELANCE, INTERNSHIP, CONTRACT
    }
}
