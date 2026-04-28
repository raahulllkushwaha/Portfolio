package com.portfolio;

import com.portfolio.dto.ProjectDto;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Project;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project sampleProject;

    @BeforeEach
    void setUp() {
        sampleProject = Project.builder()
                .title("My Portfolio")
                .description("A portfolio website")
                .status(Project.ProjectStatus.COMPLETED)
                .featured(true)
                .displayOrder(1)
                .technologies(List.of("Java", "Spring Boot"))
                .build();
        // Simulate DB-generated id
        try {
            var idField = Project.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(sampleProject, 1L);
        } catch (Exception ignored) {}
    }

    @Test
    void getAllProjects_returnsList() {
        when(projectRepository.findAllByOrderByDisplayOrderAsc()).thenReturn(List.of(sampleProject));

        List<ProjectDto.Response> result = projectService.getAllProjects();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("My Portfolio");
    }

    @Test
    void getProjectById_notFound_throwsException() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getProjectById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createProject_savesAndReturns() {
        when(projectRepository.save(any(Project.class))).thenReturn(sampleProject);

        ProjectDto.Request req = new ProjectDto.Request();
        req.setTitle("My Portfolio");
        req.setStatus(Project.ProjectStatus.COMPLETED);

        ProjectDto.Response res = projectService.createProject(req);

        assertThat(res.getTitle()).isEqualTo("My Portfolio");
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void deleteProject_notFound_throwsException() {
        when(projectRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> projectService.deleteProject(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
