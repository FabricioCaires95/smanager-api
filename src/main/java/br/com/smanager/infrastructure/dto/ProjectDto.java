package br.com.smanager.infrastructure.dto;

import br.com.smanager.domain.entity.Project;

import java.time.LocalDate;

public record ProjectDto(String id, String name, String description, LocalDate initialDate, LocalDate finalDate) {

    public static ProjectDto from(Project project) {
        return new ProjectDto(project.getId(),
                project.getName(),
                project.getDescription(),
                project.getInitialDate(),
                project.getFinalDate());
    }
}
