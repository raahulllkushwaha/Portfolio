package com.portfolio.dto;

import com.portfolio.model.Skill.SkillCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class SkillDto {

    @Data
    public static class Request {
        @NotBlank private String name;
        private SkillCategory category;
        @Min(0) @Max(100) private int proficiencyPercent;
        private String iconUrl;
        private int displayOrder;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private SkillCategory category;
        private int proficiencyPercent;
        private String iconUrl;
        private int displayOrder;
    }
}
