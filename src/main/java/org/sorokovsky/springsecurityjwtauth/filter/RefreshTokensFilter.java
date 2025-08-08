package org.sorokovsky.springsecurityjwtauth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;
import org.sorokovsky.springsecurityjwtauth.service.AuthenticationService;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.sorokovsky.springsecurityjwtauth.service.UsersService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Setter
public class RefreshTokensFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokensFilter.class);

    private final TokenStorage refreshTokenStorage;
    private final TokenDeserializer refreshTokenDeserializer;
    private final UsersService usersService;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var refreshToken = refreshTokenStorage.get().map(refreshTokenDeserializer).orElse(null);
        if (refreshToken != null) {
            UserModel user = null;
            if (authentication instanceof PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken) {
                user = (UserModel) preAuthenticatedAuthenticationToken.getPrincipal();
            }
            if (authentication == null) {
                user = usersService.getByEmail(refreshToken.email()).orElse(null);
            }
            if (user != null) {
                authenticationService.authenticate(user.getEmail(), user.getPassword());
                CsrfFilter.skipRequest(request);
            }
        } else {
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "UsernameAndPassword");
            LOGGER.debug("No refresh token found");
        }
        filterChain.doFilter(request, response);
    }
}
