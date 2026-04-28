package com.portfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String thumbnailUrl;

    private String liveDemoUrl;

    private String githubUrl;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;   // COMPLETED, IN_PROGRESS, ARCHIVED

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean featured;       // Pinned/highlighted project

    private int displayOrder;

    // Tags stored as comma-separated or use ElementCollection
    @ElementCollection
    @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> technologies;

    public enum ProjectStatus {
        COMPLETED, IN_PROGRESS, ARCHIVED
    }
}
