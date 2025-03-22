package br.com.smanager.infrastructure.dto;

import java.time.LocalDate;

public record SaveProjectDto(String name, String description, LocalDate initialDate, LocalDate finalDate, String status) {
}
