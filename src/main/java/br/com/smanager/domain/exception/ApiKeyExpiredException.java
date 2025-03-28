package br.com.smanager.domain.exception;

import br.com.smanager.infrastructure.exception.RequestException;

public class ApiKeyExpiredException extends RequestException {
    public ApiKeyExpiredException(String apiKey) {
        super("ApiKeyExpired", "api key expired: " + apiKey);
    }
}
