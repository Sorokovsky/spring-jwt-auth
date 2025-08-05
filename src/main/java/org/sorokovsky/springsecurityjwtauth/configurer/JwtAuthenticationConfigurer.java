package org.sorokovsky.springsecurityjwtauth.configurer;

import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.JwtAuthenticationConverter;
import org.sorokovsky.springsecurityjwtauth.service.TokenService;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.sorokovsky.springsecurityjwtauth.service.UsersService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

@RequiredArgsConstructor
public class JwtAuthenticationConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenStorage accessTokenStorage;
    private final TokenStorage refreshTokenStorage;
    private final TokenService tokenService;
    private final UsersService usersService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void init(HttpSecurity builder) {

    }

    @Override
    public void configure(HttpSecurity builder) {
        final var manager = builder.getSharedObject(AuthenticationManager.class);
        final var converter = new JwtAuthenticationConverter(accessTokenStorage, refreshTokenStorage, tokenService, usersService);
        final var filter = new AuthenticationFilter(manager, converter);
        filter.setFailureHandler(authenticationEntryPoint::commence);
        filter.setSuccessHandler((_, _, _) -> {
        });
        builder.addFilterAfter(filter, CsrfFilter.class);
    }
}
