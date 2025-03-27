package br.com.smanager.infrastructure.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String apiKey;


    public ApiKeyAuthenticationToken(String apiKey) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }
}
