package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;

public class TaskNotFoundException extends RequestException {
    public TaskNotFoundException(String taskId) {
        super("TaskNotFound", "Task Not found: " + taskId);
    }
}
