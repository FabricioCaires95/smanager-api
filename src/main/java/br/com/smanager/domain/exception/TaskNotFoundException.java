package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;
import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends RequestException {
    public TaskNotFoundException(String taskId) {
        super("TaskNotFound", "Task Not found: " + taskId, HttpStatus.NOT_FOUND);
    }
}
