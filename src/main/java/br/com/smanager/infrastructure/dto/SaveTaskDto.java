package br.com.smanager.infrastructure.dto;

import br.com.smanager.domain.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SaveTaskDto(
        @NotNull(message = "Title cannot be empty") String title,
        @NotNull(message = "Description cannot be empty") @Size(min = 1, max = 150, message = "Invalid description") String description,
        @NotNull(message = "Number of days cannot be empty") @Positive(message = "Number must be positive") Integer numberOfDays,
        TaskStatus status) {
}
