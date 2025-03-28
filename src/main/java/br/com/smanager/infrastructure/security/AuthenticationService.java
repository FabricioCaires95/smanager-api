package br.com.smanager.infrastructure.security;

import br.com.smanager.domain.exception.ApiKeyExpiredException;
import br.com.smanager.domain.exception.ApiKeyNotFoundException;
import br.com.smanager.domain.service.ApiKeyService;
import br.com.smanager.infrastructure.config.AppConfigProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String AUTH_TOKEN_HEADER_NAME = "x-api-key";

    private final AppConfigProperties props;
    private final ApiKeyService apiKeyService;

    public Authentication getAuthentication(HttpServletRequest request) {
        var apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);

        if (Objects.isNull(apiKey) || !Objects.equals(apiKey, props.getSecurity().getMasterApiKey())) {
            try {
                apiKeyService.validateApiKey(apiKey);
            } catch (ApiKeyNotFoundException | ApiKeyExpiredException e) {
                throw new BadCredentialsException("Api key is not valid: " + apiKey, e);
            }
        }

        return new ApiKeyAuthenticationToken(apiKey);
    }
}
