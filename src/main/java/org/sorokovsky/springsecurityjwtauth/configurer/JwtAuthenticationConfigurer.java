package org.sorokovsky.springsecurityjwtauth.configurer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.convertor.JwtAccessTokenToAuthenticationConvertor;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class JwtAuthenticationConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
    @Qualifier("jws-deserializer")
    private TokenDeserializer accessTokenDeserializer;
    @Qualifier("bearer-storage")
    private TokenStorage accessTokenStorage;

    @Override
    public void init(HttpSecurity builder) {

    }

    @Override
    public void configure(HttpSecurity builder) {
        final var authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        final var converter = new JwtAccessTokenToAuthenticationConvertor(accessTokenDeserializer, accessTokenStorage);
        final var filter = new AuthenticationFilter(authenticationManager, converter);
        filter.setFailureHandler((_, response, _) -> {
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
            response.sendError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        });
        filter.setSuccessHandler((_, _, _) -> {
        });
        builder.addFilterBefore(filter, BasicAuthenticationFilter.class);
    }
}
