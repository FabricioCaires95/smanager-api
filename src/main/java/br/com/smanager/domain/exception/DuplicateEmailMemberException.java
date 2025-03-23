package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;

public class DuplicateEmailMemberException extends RequestException {
    public DuplicateEmailMemberException(String errorCode, String message) {
        super(errorCode, message);
    }
}
