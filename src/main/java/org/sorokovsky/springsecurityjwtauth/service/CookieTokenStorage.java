package org.sorokovsky.springsecurityjwtauth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequestScope
@Qualifier("cookie-storage")
public class CookieTokenStorage implements TokenStorage {
    private static final String COOKIE_NAME = "__Host-o9uJOloVn7IBYwYoqPWZHnhqVEbNavAC";

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Duration lifetime;

    public CookieTokenStorage(
            HttpServletRequest request,
            HttpServletResponse response,
            @Qualifier("refresh-lifetime")
            Duration lifetime
    ) {
        this.request = request;
        this.response = response;
        this.lifetime = lifetime;
    }

    @Override
    public Optional<String> get() {
        final var cookies = request.getCookies();
        if (cookies == null) return Optional.empty();
        final var needCookie = Stream.of(cookies)
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst().orElse(null);
        if (needCookie == null) return Optional.empty();
        return Optional.of(needCookie.getValue());
    }

    @Override
    public void set(String token) {
        final var now = Instant.now();
        final var maxAge = (int) ChronoUnit.SECONDS.between(now, now.plus(lifetime));
        set(token, maxAge);
    }

    @Override
    public void clear() {
        set("", 0);
    }

    private void set(String value, int maxAge) {
        final var cookie = new Cookie(COOKIE_NAME, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain(null);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
