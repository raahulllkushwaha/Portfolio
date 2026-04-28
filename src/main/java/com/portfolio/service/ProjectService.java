package com.portfolio.service;

import com.portfolio.dto.ProjectDto;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Project;
import com.portfolio.model.Project.ProjectStatus;
import com.portfolio.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<ProjectDto.Response> getAllProjects() {
        return projectRepository.findAllByOrderByDisplayOrderAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProjectDto.Response> getFeaturedProjects() {
        return projectRepository.findByFeaturedTrueOrderByDisplayOrderAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProjectDto.Response> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatusOrderByDisplayOrderAsc(status)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProjectDto.Response getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return toResponse(project);
    }

    public ProjectDto.Response createProject(ProjectDto.Request request) {
        Project project = toEntity(request);
        return toResponse(projectRepository.save(project));
    }

    public ProjectDto.Response updateProject(Long id, ProjectDto.Request request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        mapRequestToEntity(request, project);
        return toResponse(projectRepository.save(project));
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    // ---- Mappers ----

    private Project toEntity(ProjectDto.Request req) {
        return Project.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .thumbnailUrl(req.getThumbnailUrl())
                .liveDemoUrl(req.getLiveDemoUrl())
                .githubUrl(req.getGithubUrl())
                .status(req.getStatus())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .featured(req.isFeatured())
                .displayOrder(req.getDisplayOrder())
                .technologies(req.getTechnologies())
                .build();
    }

    private void mapRequestToEntity(ProjectDto.Request req, Project project) {
        project.setTitle(req.getTitle());
        project.setDescription(req.getDescription());
        project.setThumbnailUrl(req.getThumbnailUrl());
        project.setLiveDemoUrl(req.getLiveDemoUrl());
        project.setGithubUrl(req.getGithubUrl());
        project.setStatus(req.getStatus());
        project.setStartDate(req.getStartDate());
        project.setEndDate(req.getEndDate());
        project.setFeatured(req.isFeatured());
        project.setDisplayOrder(req.getDisplayOrder());
        project.setTechnologies(req.getTechnologies());
    }

    private ProjectDto.Response toResponse(Project p) {
        ProjectDto.Response res = new ProjectDto.Response();
        res.setId(p.getId());
        res.setTitle(p.getTitle());
        res.setDescription(p.getDescription());
        res.setThumbnailUrl(p.getThumbnailUrl());
        res.setLiveDemoUrl(p.getLiveDemoUrl());
        res.setGithubUrl(p.getGithubUrl());
        res.setStatus(p.getStatus());
        res.setStartDate(p.getStartDate());
        res.setEndDate(p.getEndDate());
        res.setFeatured(p.isFeatured());
        res.setDisplayOrder(p.getDisplayOrder());
        res.setTechnologies(p.getTechnologies());
        return res;
    }
}
