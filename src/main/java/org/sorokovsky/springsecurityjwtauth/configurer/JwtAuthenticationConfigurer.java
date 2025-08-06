package org.sorokovsky.springsecurityjwtauth.configurer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.convertor.JwtAccessTokenToAuthenticationConvertor;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
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
    private AuthenticationEntryPoint authenticationEntryPoint = (_, _, _) -> {
    };

    @Override
    public void init(HttpSecurity builder) {

    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        final var authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        final var converter = new JwtAccessTokenToAuthenticationConvertor(accessTokenDeserializer, accessTokenStorage);
        final var filter = new AuthenticationFilter(authenticationManager, converter);
        filter.setFailureHandler(authenticationEntryPoint::commence);
        filter.setSuccessHandler((_, _, _) -> {
        });
        builder.exceptionHandling(configuration -> configuration
                .authenticationEntryPoint(authenticationEntryPoint)
        );
        builder.addFilterBefore(filter, BasicAuthenticationFilter.class);
    }
}
