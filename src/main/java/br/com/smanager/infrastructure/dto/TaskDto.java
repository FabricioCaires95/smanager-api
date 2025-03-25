package br.com.smanager.infrastructure.dto;

import br.com.smanager.domain.entity.Task;
import br.com.smanager.domain.model.TaskStatus;

public record TaskDto(
        String id,
        String title,
        String description,
        Integer numberOfDays,
        TaskStatus status,
        ProjectDto project,
        MemberDto assignedMember) {

    public static TaskDto from(Task task) {
        return new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getNumberOfDays(),
                task.getStatus(), ProjectDto.from(task.getProject()), MemberDto.from(task.getAssignedMember()));
    }
}
