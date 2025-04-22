package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends RequestException {
    public MemberNotFoundException(String memberId) {
        super("MemberNotFound", "Member Not found: " + memberId, HttpStatus.NOT_FOUND);
    }
}
