package br.com.smanager.domain.service;

import br.com.smanager.domain.entity.Project;
import br.com.smanager.domain.model.ProjectStatus;
import br.com.smanager.domain.repository.ProjectRepository;
import br.com.smanager.infrastructure.dto.SaveProjectDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public Project create(SaveProjectDto project) {
        Project newProject = Project.builder()
                .name(project.name())
                .description(project.description())
                .initialDate(project.initialDate())
                .finalDate(project.finalDate())
                .status(ProjectStatus.PENDING)
                .build();

        return projectRepository.save(newProject);
    }

}
