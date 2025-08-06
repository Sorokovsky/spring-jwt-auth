package org.sorokovsky.springsecurityjwtauth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Service
@RequestScope
@RequiredArgsConstructor
@Qualifier("bearer-storage")
public class BearerTokenStorage implements TokenStorage {
    private static final String BEARER = "Bearer ";

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Override
    public Optional<String> get() {
        final var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BEARER)) return Optional.empty();
        return Optional.of(authorization.substring(BEARER.length()));
    }

    @Override
    public void set(String token) {
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + token);
    }

    @Override
    public void clear() {
        response.setHeader(HttpHeaders.AUTHORIZATION, "");
    }
}
