package org.sorokovsky.springsecurityjwtauth.configurer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.convertor.JwtAccessTokenToAuthenticationConvertor;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class JwtAuthenticationConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
    private TokenDeserializer accessTokenDeserializer;
    private TokenStorage accessTokenStorage;
    private AuthenticationEntryPoint authenticationEntryPoint = (_, _, _) -> {
    };

    @Override
    public void init(HttpSecurity builder) throws Exception {
        builder.exceptionHandling(configurer -> configurer
                .authenticationEntryPoint(authenticationEntryPoint)
        );
    }

    @Override
    public void configure(HttpSecurity builder) {
        final var authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        final var converter = new JwtAccessTokenToAuthenticationConvertor(accessTokenDeserializer, accessTokenStorage);
        final var filter = new AuthenticationFilter(authenticationManager, converter);
        filter.setSuccessHandler((_, _, _) -> {
        });
        builder.addFilterBefore(filter, BasicAuthenticationFilter.class);
    }
}
