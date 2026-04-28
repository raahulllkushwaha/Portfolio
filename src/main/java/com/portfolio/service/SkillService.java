package com.portfolio.service;

import com.portfolio.dto.SkillDto;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Skill;
import com.portfolio.model.Skill.SkillCategory;
import com.portfolio.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public List<SkillDto.Response> getAllSkills() {
        return skillRepository.findAllByOrderByDisplayOrderAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<SkillDto.Response> getSkillsByCategory(SkillCategory category) {
        return skillRepository.findByCategoryOrderByDisplayOrderAsc(category)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public SkillDto.Response getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
        return toResponse(skill);
    }

    public SkillDto.Response createSkill(SkillDto.Request request) {
        return toResponse(skillRepository.save(toEntity(request)));
    }

    public SkillDto.Response updateSkill(Long id, SkillDto.Request request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
        mapRequestToEntity(request, skill);
        return toResponse(skillRepository.save(skill));
    }

    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Skill not found with id: " + id);
        }
        skillRepository.deleteById(id);
    }

    // ---- Mappers ----

    private Skill toEntity(SkillDto.Request req) {
        return Skill.builder()
                .name(req.getName())
                .category(req.getCategory())
                .proficiencyPercent(req.getProficiencyPercent())
                .iconUrl(req.getIconUrl())
                .displayOrder(req.getDisplayOrder())
                .build();
    }

    private void mapRequestToEntity(SkillDto.Request req, Skill skill) {
        skill.setName(req.getName());
        skill.setCategory(req.getCategory());
        skill.setProficiencyPercent(req.getProficiencyPercent());
        skill.setIconUrl(req.getIconUrl());
        skill.setDisplayOrder(req.getDisplayOrder());
    }

    private SkillDto.Response toResponse(Skill s) {
        SkillDto.Response res = new SkillDto.Response();
        res.setId(s.getId());
        res.setName(s.getName());
        res.setCategory(s.getCategory());
        res.setProficiencyPercent(s.getProficiencyPercent());
        res.setIconUrl(s.getIconUrl());
        res.setDisplayOrder(s.getDisplayOrder());
        return res;
    }
}
