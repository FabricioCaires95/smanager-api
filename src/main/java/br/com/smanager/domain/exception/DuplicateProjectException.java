package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;

public class DuplicateProjectException extends RequestException {
    public DuplicateProjectException(String errorCode, String message) {
        super(errorCode, message);
    }
}
