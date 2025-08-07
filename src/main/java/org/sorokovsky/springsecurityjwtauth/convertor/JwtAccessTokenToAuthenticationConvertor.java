package org.sorokovsky.springsecurityjwtauth.convertor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
public class JwtAccessTokenToAuthenticationConvertor implements AuthenticationConverter {
    private final Logger LOGGER = LoggerFactory.getLogger(JwtAccessTokenToAuthenticationConvertor.class);

    private final TokenDeserializer accessTokenDeserializer;
    private final TokenStorage accessTokenStorage;

    @Override
    public Authentication convert(HttpServletRequest request) {
        final var stringAccessToken = accessTokenStorage.get().orElse(null);
        if(stringAccessToken != null) {
            final var accessToken = accessTokenDeserializer.apply(stringAccessToken);
            LOGGER.info("{} access token has been stored.", accessToken);
            return new PreAuthenticatedAuthenticationToken(accessToken, stringAccessToken);
        }
        LOGGER.warn("No access token has been stored.");
        return null;
    }
}
