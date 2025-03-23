package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;

public class InvalidProjectStatusException extends RequestException {
    public InvalidProjectStatusException(String message) {
        super("InvalidProjectStatus", "Invalid project status: " + message);
    }
}
