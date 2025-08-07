package org.sorokovsky.springsecurityjwtauth.strategy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorokovsky.springsecurityjwtauth.factory.AccessTokenFactory;
import org.sorokovsky.springsecurityjwtauth.factory.RefreshTokenFactory;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;
import org.sorokovsky.springsecurityjwtauth.serializer.TokenSerializer;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@RequiredArgsConstructor
public class JwtAuthenticationStrategy implements SessionAuthenticationStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationStrategy.class);

    private final AccessTokenFactory accessTokenFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final TokenSerializer accessTokenSerializer;
    private final TokenSerializer refreshTokenSerializer;
    private final TokenStorage accessTokenStorage;
    private final TokenStorage refreshTokenStorage;

    @Override
    public void onAuthentication(
            Authentication authentication, HttpServletRequest request, HttpServletResponse response)
            throws SessionAuthenticationException {
        System.out.println(authentication);
        if (authentication instanceof UsernamePasswordAuthenticationToken userAuthentication) {
            final var principal = userAuthentication.getPrincipal();
            if (principal instanceof UserModel user) {
                final var refreshToken = refreshTokenFactory.apply(user);
                final var accessToken = accessTokenFactory.apply(refreshToken);
                accessTokenStorage.set(accessTokenSerializer.apply(accessToken));
                refreshTokenStorage.set(refreshTokenSerializer.apply(refreshToken));
                LOGGER.info("Tokens of {} was stored.", user);
            }
        }
    }
}
