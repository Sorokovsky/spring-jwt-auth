package org.sorokovsky.springsecurityjwtauth.configurer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorokovsky.springsecurityjwtauth.convertor.JwtAccessTokenToAuthenticationConvertor;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.factory.AccessTokenFactory;
import org.sorokovsky.springsecurityjwtauth.serializer.TokenSerializer;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class JwtAuthenticationConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationConfigurer.class);

    private TokenDeserializer accessTokenDeserializer;
    private TokenStorage accessTokenStorage;
    private AuthenticationEntryPoint authenticationEntryPoint = (_, _, _) -> {};
    private PreAuthenticatedAuthenticationProvider authenticationProvider;
    private TokenStorage refreshTokenStorage;
    private TokenDeserializer refreshTokenDeserializer;
    private AccessTokenFactory accessTokenFactory;
    private TokenSerializer accessTokenSerializer;

    @Override
    public void init(HttpSecurity builder) {
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        try {
            final var authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            final var filter = getAuthenticationFilter(authenticationManager);
            builder
                    .exceptionHandling(configurer -> configurer
                            .authenticationEntryPoint(authenticationEntryPoint)
                    )
                    .addFilterAfter(filter, CsrfFilter.class)
                    .authenticationProvider(authenticationProvider);
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        final var converter = new JwtAccessTokenToAuthenticationConvertor(
                accessTokenDeserializer,
                accessTokenStorage,
                refreshTokenStorage,
                refreshTokenDeserializer,
                accessTokenFactory,
                accessTokenSerializer
        );
        final var filter = new AuthenticationFilter(authenticationManager, converter);
        filter.setSuccessHandler((_, _, authentication) -> {
            LOGGER.info("{} is authenticated successfully.", authentication.getPrincipal());
        });
        return filter;
    }
}
