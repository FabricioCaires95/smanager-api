package br.com.smanager.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.util.List;

@RequiredArgsConstructor
public class AuthenticatorFilter extends GenericFilterBean {

    private static final List<String> WHITELIST = List.of(
            "/v3/api-docs",
            "/v3/api-docs/",
            "/v3/api-docs/",
            "/swagger-ui",
            "/swagger-ui/",
            "/swagger-ui.html/**",
            "/actuator",
            "/actuator/"
    );

    private final AuthenticationService authenticationService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            String path = ((HttpServletRequest) servletRequest).getRequestURI();

            if (isWhitelisted(path)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            var authentication = authenticationService.getAuthentication((HttpServletRequest) servletRequest);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            var response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean isWhitelisted(String path) {
        return WHITELIST.stream().anyMatch(path::startsWith);
    }
}
