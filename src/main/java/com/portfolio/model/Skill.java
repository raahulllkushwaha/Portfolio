package com.portfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;            // e.g. "Java", "React", "Docker"

    @Enumerated(EnumType.STRING)
    private SkillCategory category; // FRONTEND, BACKEND, DATABASE, DEVOPS, OTHER

    @Min(0) @Max(100)
    private int proficiencyPercent; // 0–100

    private String iconUrl;         // Optional logo/icon URL

    private int displayOrder;

    public enum SkillCategory {
        FRONTEND, BACKEND, DATABASE, DEVOPS, TOOLS, OTHER
    }
}
