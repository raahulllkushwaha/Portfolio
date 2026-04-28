package com.portfolio.service;

import com.portfolio.dto.EducationDto;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Education;
import com.portfolio.repository.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationService {

    @Autowired
    private EducationRepository educationRepository;

    public List<EducationDto.Response> getAllEducation() {
        return educationRepository.findAllByOrderByDisplayOrderAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public EducationDto.Response getEducationById(Long id) {
        Education edu = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + id));
        return toResponse(edu);
    }

    public EducationDto.Response createEducation(EducationDto.Request request) {
        return toResponse(educationRepository.save(toEntity(request)));
    }

    public EducationDto.Response updateEducation(Long id, EducationDto.Request request) {
        Education edu = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + id));
        mapRequestToEntity(request, edu);
        return toResponse(educationRepository.save(edu));
    }

    public void deleteEducation(Long id) {
        if (!educationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Education not found with id: " + id);
        }
        educationRepository.deleteById(id);
    }

    // ---- Mappers ----

    private Education toEntity(EducationDto.Request req) {
        return Education.builder()
                .degree(req.getDegree())
                .institution(req.getInstitution())
                .institutionLogoUrl(req.getInstitutionLogoUrl())
                .location(req.getLocation())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .currentlyStudying(req.isCurrentlyStudying())
                .grade(req.getGrade())
                .description(req.getDescription())
                .displayOrder(req.getDisplayOrder())
                .build();
    }

    private void mapRequestToEntity(EducationDto.Request req, Education edu) {
        edu.setDegree(req.getDegree());
        edu.setInstitution(req.getInstitution());
        edu.setInstitutionLogoUrl(req.getInstitutionLogoUrl());
        edu.setLocation(req.getLocation());
        edu.setStartDate(req.getStartDate());
        edu.setEndDate(req.getEndDate());
        edu.setCurrentlyStudying(req.isCurrentlyStudying());
        edu.setGrade(req.getGrade());
        edu.setDescription(req.getDescription());
        edu.setDisplayOrder(req.getDisplayOrder());
    }

    private EducationDto.Response toResponse(Education e) {
        EducationDto.Response res = new EducationDto.Response();
        res.setId(e.getId());
        res.setDegree(e.getDegree());
        res.setInstitution(e.getInstitution());
        res.setInstitutionLogoUrl(e.getInstitutionLogoUrl());
        res.setLocation(e.getLocation());
        res.setStartDate(e.getStartDate());
        res.setEndDate(e.getEndDate());
        res.setCurrentlyStudying(e.isCurrentlyStudying());
        res.setGrade(e.getGrade());
        res.setDescription(e.getDescription());
        res.setDisplayOrder(e.getDisplayOrder());
        return res;
    }
}
