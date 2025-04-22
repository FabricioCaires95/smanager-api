package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;
import org.springframework.http.HttpStatus;

public class ApiKeyNotFoundException extends RequestException {
    public ApiKeyNotFoundException(String apiKey) {
        super("ApiKeyNotFound", "api key Not found: " + apiKey, HttpStatus.NOT_FOUND);
    }
}
