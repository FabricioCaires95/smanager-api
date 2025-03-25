package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;

public class InvalidTaskStatusException extends RequestException {
    public InvalidTaskStatusException(String message) {
        super("InvalidTaskStatusException", "Invalid task status: " + message);
    }
}
