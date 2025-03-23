package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;

public class ProjectNotFoundException extends RequestException {
    public ProjectNotFoundException(String projectId) {
        super("ProjectNotFound", "Prooject Not found: " + projectId);
    }
}
