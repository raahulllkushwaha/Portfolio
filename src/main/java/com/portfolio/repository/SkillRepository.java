package com.portfolio.repository;

import com.portfolio.model.Skill;
import com.portfolio.model.Skill.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByOrderByDisplayOrderAsc();
    List<Skill> findByCategoryOrderByDisplayOrderAsc(SkillCategory category);
}
