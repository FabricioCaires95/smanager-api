package br.com.smanager.infrastructure.dto;

import br.com.smanager.domain.entity.Member;
import br.com.smanager.domain.entity.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record ProjectDto(
        String id,
        String name,
        String description,
        LocalDate initialDate,
        LocalDate finalDate,
        String status,
        Set<String> members) {

    public static ProjectDto from(Project project) {
        return new ProjectDto(project.getId(),
                project.getName(),
                project.getDescription(),
                project.getInitialDate(),
                project.getFinalDate(),
                project.getStatus().name(),
                Optional
                        .ofNullable(project.getMembers()).orElse(List.of())
                        .stream()
                        .map(Member::getId)
                        .collect(Collectors.toSet())
        );
    }
}
