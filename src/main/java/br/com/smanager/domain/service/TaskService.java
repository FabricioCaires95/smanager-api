package br.com.smanager.domain.service;

import br.com.smanager.domain.entity.Member;
import br.com.smanager.domain.entity.Project;
import br.com.smanager.domain.entity.Task;
import br.com.smanager.domain.exception.InvalidTaskStatusException;
import br.com.smanager.domain.exception.TaskNotFoundException;
import br.com.smanager.domain.model.TaskStatus;
import br.com.smanager.domain.repository.TaskRepository;
import br.com.smanager.infrastructure.config.AppConfigProperties;
import br.com.smanager.infrastructure.dto.SaveTaskDto;
import br.com.smanager.infrastructure.util.PaginationHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final AppConfigProperties props;

    @Transactional
    public Task create(SaveTaskDto taskDto) {
        var project = getProjectIfExists(taskDto.projectId());
        var member = getMemberIfExists(taskDto.memberId());

        var task = Task.builder()
                .title(taskDto.title())
                .description(taskDto.description())
                .numberOfDays(taskDto.numberOfDays())
                .status(TaskStatus.PENDING)
                .project(project)
                .assignedMember(member)
                .build();

        return taskRepository.save(task);
    }



    public Task loadById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public void deleteTask(String id) {
        var task = loadById(id);
        taskRepository.delete(task);
    }

    @Transactional
    public Task update(String id, SaveTaskDto taskDto) {
        var task = loadById(id);

        var project = getProjectIfExists(taskDto.projectId());
        var member = getMemberIfExists(taskDto.memberId());

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setNumberOfDays(taskDto.numberOfDays());
        task.setStatus(taskDto.status());
        task.setProject(project);
        task.setAssignedMember(member);

        return task;
    }

    public Page<Task> findTasks(
            String projectId,
            String memberId,
            String status,
            String partialTitle,
            Integer page,
            String direction,
            List<String> properties
    ) {
        return taskRepository.find(
                projectId,
                memberId,
                Optional.ofNullable(status).map(this::convertTaskStatus).orElse(null),
                partialTitle,
                PaginationHelper.createPageable(page, props.getGeneral().getPageSize(), direction, properties));
    }

    private Project getProjectIfExists(String projectId) {
        Project project = null;
        if (Objects.nonNull(projectId)) {
            project = projectService.getById(projectId);
        }
        return project;
    }

    private Member getMemberIfExists(String memberId) {
        Member member = null;
        if (Objects.nonNull(memberId)) {
            member = memberService.loadMemberById(memberId);
        }
        return member;
    }

    private TaskStatus convertTaskStatus(String status) {
        try {
            return TaskStatus.valueOf(status);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidTaskStatusException(status);
        }
    }
}

