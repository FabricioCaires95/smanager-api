package br.com.smanager.infrastructure.security;

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

    public Authentication getAuthentication(HttpServletRequest request) {
        var apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);

        if (Objects.isNull(apiKey) || !Objects.equals(apiKey, props.getSecurity().getApiKey())) {
            throw new BadCredentialsException("Invalid API Key: " + apiKey);
        }

        return new ApiKeyAuthenticationToken(apiKey);
    }
}
