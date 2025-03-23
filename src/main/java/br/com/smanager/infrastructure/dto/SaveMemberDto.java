package br.com.smanager.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SaveMemberDto(@NotNull(message = "name cannot be empty") @Size(min = 1, max = 80, message = "Invalid member name") String name,
                            @Email(message = "Email is not valid") String email) {
}
