package org.sorokovsky.springsecurityjwtauth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.service.TokenService;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.sorokovsky.springsecurityjwtauth.service.UsersService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
public class JwtAuthenticationConverter implements AuthenticationConverter {
    private final TokenStorage accessTokenStorage;
    private final TokenStorage refreshTokenStorage;
    private final TokenService tokenService;
    private final UsersService usersService;


    @Override
    public Authentication convert(HttpServletRequest request) {
        var accessToken = accessTokenStorage.get().orElse(null);
        var refreshToken = refreshTokenStorage.get().orElse(null);
        if (accessToken == null && refreshToken == null) {
            return null;
        } else {
            final var activeToken = accessToken == null ? refreshToken : accessToken;
            var user = usersService.findByEmail(activeToken.email()).orElse(null);
            if (user == null) return null;
            accessToken = tokenService.generateAccessToken(user);
            refreshToken = tokenService.generateRefreshToken(user);
            accessTokenStorage.set(accessToken);
            refreshTokenStorage.set(refreshToken);
            return new PreAuthenticatedAuthenticationToken(accessToken, refreshToken);
        }
    }
}
