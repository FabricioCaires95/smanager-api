package br.com.smanager.domain.service;

import br.com.smanager.domain.document.ApiKey;
import br.com.smanager.domain.exception.ApiKeyExpiredException;
import br.com.smanager.domain.exception.ApiKeyNotFoundException;
import br.com.smanager.domain.repository.ApiKeyRepository;
import br.com.smanager.infrastructure.config.AppConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final AppConfigProperties props;

    public ApiKey createApiKey(){
        ApiKey apiKey = ApiKey.builder()
                .value(UUID.randomUUID().toString())
                .expirationWhen(
                        OffsetDateTime
                            .now()
                            .plusDays(props.getSecurity().getExpirationDays())
                            .toInstant())
                .build();
        return apiKeyRepository.save(apiKey);
    }

    public void revokeApiKey(String id){
        var apiKey = loadApiKeyById(id);
        apiKey.setExpirationWhen(Instant.EPOCH);
        apiKeyRepository.save(apiKey);
    }

    public void validateApiKey(String value){
        var apiKey = loadApiKeyByValue(value);

        if (apiKey.getExpirationWhen().isBefore(Instant.now())){
            throw new ApiKeyExpiredException("Invalid API Key: " + value);
        }
    }

    private ApiKey loadApiKeyById(String id){
        return apiKeyRepository
                .findById(id)
                .orElseThrow(() -> new ApiKeyNotFoundException(id));
    }

    private ApiKey loadApiKeyByValue(String value){
        return apiKeyRepository
                .findByValue(value)
                .orElseThrow(() -> new ApiKeyNotFoundException(value));
    }
}
