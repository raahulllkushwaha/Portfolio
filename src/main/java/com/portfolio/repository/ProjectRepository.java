package com.portfolio.repository;

import com.portfolio.model.Project;
import com.portfolio.model.Project.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOrderByDisplayOrderAsc();
    List<Project> findByFeaturedTrueOrderByDisplayOrderAsc();
    List<Project> findByStatusOrderByDisplayOrderAsc(ProjectStatus status);
}
