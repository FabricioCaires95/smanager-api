package br.com.smanager.domain.service;

import br.com.smanager.domain.entity.Project;
import br.com.smanager.domain.exception.InvalidProjectStatusException;
import br.com.smanager.domain.exception.ProjectNotFoundException;
import br.com.smanager.domain.model.ProjectStatus;
import br.com.smanager.domain.repository.ProjectRepository;
import br.com.smanager.infrastructure.dto.ProjectDto;
import br.com.smanager.infrastructure.dto.SaveProjectDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectDto create(SaveProjectDto projectDto) {
        Project newProject = Project.builder()
                .name(projectDto.name())
                .description(projectDto.description())
                .initialDate(projectDto.initialDate())
                .finalDate(projectDto.finalDate())
                .status(ProjectStatus.PENDING)
                .build();

        var project = projectRepository.save(newProject);
        return ProjectDto.from(project);
    }

    public Project getById(String id) {
        return projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
    }

    @Transactional
    public void delete(String projectId) {
        var project = getById(projectId);
        projectRepository.delete(project);
    }

    @Transactional
    public Project updateProject(String id, SaveProjectDto projectDto) {
        var project = getById(id);

        project.setName(projectDto.name());
        project.setDescription(projectDto.description());
        project.setInitialDate(projectDto.initialDate());
        project.setFinalDate(projectDto.finalDate());
        project.setStatus(convertStatus(projectDto.status()));

        return project;
    }

    private ProjectStatus convertStatus(String statusStr) {
        try {
            return ProjectStatus.valueOf(statusStr);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidProjectStatusException(statusStr);
        }
    }
}
