package org.sorokovsky.springsecurityjwtauth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.serializer.TokenSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Service("bearer-storage")
@RequiredArgsConstructor
@RequestScope
public class BearerTokenStorage implements TokenStorage{
    private final static String BEARER = "Bearer ";
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final TokenDeserializer deserializer;
    private final TokenSerializer serializer;

    @Override
    public Optional<Token> get() {
        final var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization == null || !authorization.startsWith(BEARER)) {
            return Optional.empty();
        } else {
            final var stringToken = authorization.substring(BEARER.length());
            return Optional.ofNullable(deserializer.apply(stringToken));
        }
    }

    @Override
    public void set(Token token) {
        final var stringToken = serializer.apply(token);
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + stringToken);
    }

    @Override
    public void clear() {
        response.setHeader(HttpHeaders.AUTHORIZATION, "");
    }
}