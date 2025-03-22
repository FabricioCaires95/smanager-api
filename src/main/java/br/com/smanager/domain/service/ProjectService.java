package br.com.smanager.domain.service;

import br.com.smanager.domain.entity.Project;
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

}
