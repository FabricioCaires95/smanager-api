package br.com.smanager.domain.service;

import br.com.smanager.domain.entity.Project;
import br.com.smanager.domain.exception.DuplicateProjectException;
import br.com.smanager.domain.exception.InvalidProjectStatusException;
import br.com.smanager.domain.exception.ProjectNotFoundException;
import br.com.smanager.domain.model.ProjectStatus;
import br.com.smanager.domain.repository.ProjectRepository;
import br.com.smanager.infrastructure.dto.ProjectDto;
import br.com.smanager.infrastructure.dto.SaveProjectDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberService memberService;

    @Transactional
    public ProjectDto create(SaveProjectDto projectDto) {
        existsProjectWithName(projectDto.name(), null);

        Project newProject = Project.builder()
                .name(projectDto.name())
                .description(projectDto.description())
                .initialDate(projectDto.initialDate())
                .finalDate(projectDto.finalDate())
                .status(ProjectStatus.PENDING)
                .build();

        var project = projectRepository.save(newProject);

        addMembersToProject(projectDto.members(), project);

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
        existsProjectWithName(projectDto.name(), id);

        var project = getById(id);

        project.setName(projectDto.name());
        project.setDescription(projectDto.description());
        project.setInitialDate(projectDto.initialDate());
        project.setFinalDate(projectDto.finalDate());
        project.setStatus(convertStatus(projectDto.status()));

        addMembersToProject(projectDto.members(), project);
        return project;
    }

    private ProjectStatus convertStatus(String statusStr) {
        try {
            return ProjectStatus.valueOf(statusStr);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidProjectStatusException(statusStr);
        }
    }

    private void existsProjectWithName(String name, String idToExclude) {
         var isProjectExist =  projectRepository.findProjectByName(name)
                .filter(project -> !Objects.equals(project.getId(), idToExclude))
                .isPresent();

        if (isProjectExist) {
            throw new DuplicateProjectException("DuplicatedProject", "This project name already exists:" + name);
        }
    }

    private void addMembersToProject(Set<String> membersId, Project project) {
        var members = Optional
                .ofNullable(membersId)
                .orElse(Set.of())
                .stream()
                .map(memberService::loadMemberById)
                .collect(Collectors.toList());

        project.setMembers(members);
    }
}
