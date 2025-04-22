package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;
import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends RequestException {
    public ProjectNotFoundException(String projectId) {
        super("ProjectNotFound", "Project Not found: " + projectId, HttpStatus.NOT_FOUND);
    }
}
