package br.com.smanager.infrastructure.dto;

import br.com.smanager.domain.document.ApiKey;

import java.time.Instant;

public record ApiKeyDto(String id, String value, Instant expiresWhen) {

    public static ApiKeyDto from(ApiKey apiKey){
        return new ApiKeyDto(apiKey.getId(), apiKey.getValue(), apiKey.getExpirationWhen());
    }
}
