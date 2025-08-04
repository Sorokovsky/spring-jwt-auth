package org.sorokovsky.springsecurityjwtauth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.exception.TokenNotParsedException;
import org.sorokovsky.springsecurityjwtauth.serializer.TokenSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

@Qualifier("cookie-storage")
@Service
@RequiredArgsConstructor
@RequestScope
@Setter
public class CookieTokenStorage implements TokenStorage {
    private String cookieName = "__Host-9YBIonpHN6itWA1YUONStq4aMseuNpB6";
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final TokenSerializer serializer;
    private final TokenDeserializer deserializer;

    @Override
    public Optional<Token> get() {
        final var cookies = request.getCookies();
        if (cookies == null) return Optional.empty();
        final var cookie = Arrays
                .stream(cookies).filter(item -> item.getName().equals(cookieName))
                .findFirst()
                .orElse(null);
        if(cookie == null) return Optional.empty();
        final var tokenString = cookie.getValue();
        try {
            return Optional.ofNullable(deserializer.apply(tokenString));
        } catch (TokenNotParsedException _) {
            return Optional.empty();
        }
    }

    @Override
    public void set(Token token) {
        final var stringToken = serializer.apply(token);
        final var maxAge = (int) ChronoUnit.SECONDS.between(token.createdAt(), token.expiresAt());
        set(stringToken, maxAge);
    }

    @Override
    public void clear() {
        set("", 0);
    }

    private void set(String value, int maxAge) {
        final var cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain(null);
        response.addCookie(cookie);
    }
}
