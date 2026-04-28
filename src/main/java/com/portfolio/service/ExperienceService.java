package com.portfolio.service;

import com.portfolio.dto.ExperienceDto;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Experience;
import com.portfolio.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;

    public List<ExperienceDto.Response> getAllExperiences() {
        return experienceRepository.findAllByOrderByDisplayOrderAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ExperienceDto.Response getExperienceById(Long id) {
        Experience exp = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));
        return toResponse(exp);
    }

    public ExperienceDto.Response createExperience(ExperienceDto.Request request) {
        return toResponse(experienceRepository.save(toEntity(request)));
    }

    public ExperienceDto.Response updateExperience(Long id, ExperienceDto.Request request) {
        Experience exp = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));
        mapRequestToEntity(request, exp);
        return toResponse(experienceRepository.save(exp));
    }

    public void deleteExperience(Long id) {
        if (!experienceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Experience not found with id: " + id);
        }
        experienceRepository.deleteById(id);
    }

    // ---- Mappers ----

    private Experience toEntity(ExperienceDto.Request req) {
        return Experience.builder()
                .jobTitle(req.getJobTitle())
                .company(req.getCompany())
                .companyLogoUrl(req.getCompanyLogoUrl())
                .location(req.getLocation())
                .employmentType(req.getEmploymentType())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .currentJob(req.isCurrentJob())
                .description(req.getDescription())
                .displayOrder(req.getDisplayOrder())
                .build();
    }

    private void mapRequestToEntity(ExperienceDto.Request req, Experience exp) {
        exp.setJobTitle(req.getJobTitle());
        exp.setCompany(req.getCompany());
        exp.setCompanyLogoUrl(req.getCompanyLogoUrl());
        exp.setLocation(req.getLocation());
        exp.setEmploymentType(req.getEmploymentType());
        exp.setStartDate(req.getStartDate());
        exp.setEndDate(req.getEndDate());
        exp.setCurrentJob(req.isCurrentJob());
        exp.setDescription(req.getDescription());
        exp.setDisplayOrder(req.getDisplayOrder());
    }

    private ExperienceDto.Response toResponse(Experience e) {
        ExperienceDto.Response res = new ExperienceDto.Response();
        res.setId(e.getId());
        res.setJobTitle(e.getJobTitle());
        res.setCompany(e.getCompany());
        res.setCompanyLogoUrl(e.getCompanyLogoUrl());
        res.setLocation(e.getLocation());
        res.setEmploymentType(e.getEmploymentType());
        res.setStartDate(e.getStartDate());
        res.setEndDate(e.getEndDate());
        res.setCurrentJob(e.isCurrentJob());
        res.setDescription(e.getDescription());
        res.setDisplayOrder(e.getDisplayOrder());
        return res;
    }
}
