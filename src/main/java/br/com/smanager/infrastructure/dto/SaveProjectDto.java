package br.com.smanager.infrastructure.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record SaveProjectDto(
        @NotNull(message = "name cannot be empty") @Size(min = 1, max = 80, message = "Invalid name") String name,
        @NotNull(message = "description cannot be empty") @Size(min = 1, max = 150, message = "Invalid description") String description,
        @NotNull(message = "InitialDate cannot be empty") LocalDate initialDate,
        @NotNull(message = "finalDate cannot be empty") LocalDate finalDate,
        String status,
        Set<String> members) {


    @AssertTrue(message = "InitialDate must be before finalDate")
    private boolean isInitialDateBeforeFinalDate() {
        return initialDate.isBefore(finalDate);
    }
}
