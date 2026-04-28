package com.portfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "education")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String degree;          // e.g. "B.Tech in Computer Science"

    @NotBlank
    private String institution;

    private String institutionLogoUrl;

    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean currentlyStudying;

    private String grade;           // e.g. "8.5 CGPA" or "First Class"

    @Column(columnDefinition = "TEXT")
    private String description;     // Activities, achievements, etc.

    private int displayOrder;
}
