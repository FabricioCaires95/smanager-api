package br.com.smanager.domain.service;

import br.com.smanager.domain.document.ApiKey;
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

    private ApiKey loadApiKeyById(String id){
        return apiKeyRepository
                .findById(id)
                .orElseThrow(() -> new ApiKeyNotFoundException(id));
    }
}
