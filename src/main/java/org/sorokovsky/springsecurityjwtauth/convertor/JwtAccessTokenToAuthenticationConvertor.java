package org.sorokovsky.springsecurityjwtauth.convertor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
public class JwtAccessTokenToAuthenticationConvertor implements AuthenticationConverter {
    private final TokenDeserializer accessTokenDeserializer;
    private final TokenStorage accessTokenStorage;

    @Override
    public Authentication convert(HttpServletRequest request) {
        final var stringAccessToken = accessTokenStorage.get().orElse(null);
        if (stringAccessToken == null) return null;
        final var accessToken = accessTokenDeserializer.apply(stringAccessToken);
        if (accessToken == null) return null;
        return new PreAuthenticatedAuthenticationToken(accessToken, stringAccessToken);
    }
}
