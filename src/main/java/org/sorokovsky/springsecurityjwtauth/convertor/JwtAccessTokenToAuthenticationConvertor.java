package org.sorokovsky.springsecurityjwtauth.convertor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.factory.AccessTokenFactory;
import org.sorokovsky.springsecurityjwtauth.serializer.TokenSerializer;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.csrf.CsrfFilter;

@RequiredArgsConstructor
public class JwtAccessTokenToAuthenticationConvertor implements AuthenticationConverter {
    private final Logger LOGGER = LoggerFactory.getLogger(JwtAccessTokenToAuthenticationConvertor.class);

    private final TokenDeserializer accessTokenDeserializer;
    private final TokenStorage accessTokenStorage;
    private final TokenStorage refreshTokenStorage;
    private final TokenDeserializer refreshTokenDeserializer;
    private final AccessTokenFactory accessTokenFactory;
    private final TokenSerializer accessTokenSerializer;


    @Override
    public Authentication convert(HttpServletRequest request) {
        final var accessToken = accessTokenStorage.get().map(accessTokenDeserializer).orElse(null);

        if (accessToken != null) {
            CsrfFilter.skipRequest(request);
            LOGGER.info("{} access token has been stored.", accessToken);
            return new PreAuthenticatedAuthenticationToken(accessToken, accessToken);
        } else {
            refreshTokenStorage
                    .get()
                    .map(refreshTokenDeserializer)
                    .ifPresent(refreshToken -> accessTokenStorage.set(accessTokenSerializer.apply(accessTokenFactory.apply(refreshToken))));
        }
        LOGGER.warn("No access token has been stored.");
        return null;
    }
}
