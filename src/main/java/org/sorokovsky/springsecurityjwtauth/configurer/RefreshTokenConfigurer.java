package org.sorokovsky.springsecurityjwtauth.configurer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.filter.RefreshTokensFilter;
import org.sorokovsky.springsecurityjwtauth.service.AuthenticationService;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.sorokovsky.springsecurityjwtauth.service.UsersService;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RefreshTokenConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
    private TokenStorage refreshTokenStorage;
    private TokenDeserializer refreshTokenDeserializer;
    private UsersService usersService;
    private AuthenticationService authenticationService;

    @Override
    public void init(HttpSecurity builder) {

    }

    @Override
    public void configure(HttpSecurity builder) {
        final var filter = new RefreshTokensFilter(refreshTokenStorage, refreshTokenDeserializer, usersService, authenticationService);
        builder.addFilterBefore(filter, AuthenticationFilter.class);
    }
}
