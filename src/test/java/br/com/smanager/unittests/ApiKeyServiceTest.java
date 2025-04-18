package br.com.smanager.unittests;

import br.com.smanager.domain.document.ApiKey;
import br.com.smanager.domain.exception.ApiKeyExpiredException;
import br.com.smanager.domain.repository.ApiKeyRepository;
import br.com.smanager.domain.service.ApiKeyService;
import br.com.smanager.infrastructure.config.AppConfigProperties;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Instant;
import java.util.Optional;

import static br.com.smanager.utils.TestHelper.API_KEY_ID;
import static br.com.smanager.utils.TestHelper.API_KEY_TEST;
import static br.com.smanager.utils.TestHelper.getApiKey;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ApiKeyServiceTest extends UniteTestConfig {

    @InjectMocks
    private ApiKeyService apiKeyService;

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private AppConfigProperties appConfigProperties;

    @Mock
    private AppConfigProperties.Security securityProperties;

    @Test
    public void should_create_and_return_valid_api_key(){
        when(appConfigProperties.getSecurity()).thenReturn(securityProperties);
        when(securityProperties.getExpirationDays()).thenReturn(2);
        when(apiKeyRepository.save(any())).thenReturn(getApiKey());

        var apiKey = apiKeyService.createApiKey();

        assertNotNull(apiKey);
        assertNotNull(apiKey.getId());
        assertNotNull(apiKey.getValue());
        assertNotNull(apiKey.getCreationWhen());
        assertNotNull(apiKey.getExpirationWhen());
    }

    @Test
    public void should_revoke_api_key_and_make_it_invalid(){
        var validApiKey = getApiKey();

        when(apiKeyRepository.findById(anyString())).thenReturn(Optional.of(validApiKey));

        apiKeyService.revokeApiKey(API_KEY_ID);

        assertEquals(Instant.EPOCH, validApiKey.getExpirationWhen());
    }

    @Test
    public void should_throw_ApiKeyExpiredException_for_invalid_api_key() {
        var invalidApiKey = ApiKey.builder()
                .expirationWhen(Instant.MIN)
                .build();

        when(apiKeyRepository.findByValue(anyString())).thenReturn(Optional.of(invalidApiKey));

        assertThrows(ApiKeyExpiredException.class, () -> apiKeyService.validateApiKey(API_KEY_TEST));
    }

    @Test
    public void any_exception_should_have_been_thrown_for_valid_api_key() {
        var validApiKey = getApiKey();

        when(apiKeyRepository.findByValue(anyString())).thenReturn(Optional.of(validApiKey));

        assertDoesNotThrow(() -> apiKeyService.validateApiKey(API_KEY_TEST));
    }


}
