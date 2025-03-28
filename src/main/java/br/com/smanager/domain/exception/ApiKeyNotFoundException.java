package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;

public class ApiKeyNotFoundException extends RequestException {
    public ApiKeyNotFoundException(String apiKey) {
        super("ApiKeyNotFound", "api key Not found: " + apiKey);
    }
}
