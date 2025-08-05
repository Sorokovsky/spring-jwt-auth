package org.sorokovsky.springsecurityjwtauth.config;

import org.sorokovsky.springsecurityjwtauth.configurer.JwtAuthenticationConfigurer;
import org.sorokovsky.springsecurityjwtauth.entrypoint.JwtAuthenticationEntryPoint;
import org.sorokovsky.springsecurityjwtauth.service.PreAuthenticationUserDetailsService;
import org.sorokovsky.springsecurityjwtauth.service.TokenService;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.sorokovsky.springsecurityjwtauth.service.UsersService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider,
            JwtAuthenticationConfigurer jwtAuthenticationConfigurer,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    )
            throws Exception {
        http
                .authorizeHttpRequests(manager -> manager
                        .requestMatchers("authorization/register", "/authorization/login").anonymous()
                        .requestMatchers("/authorization/logout", "/authorization/me").authenticated()
                        .requestMatchers("/v3/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.
                        authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authenticationProvider(authenticationProvider)
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer
                        .csrfTokenRepository(new CookieCsrfTokenRepository())
                        .ignoringRequestMatchers("/authorization/login", "/authorization/logout", "/authorization/register")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.apply(jwtAuthenticationConfigurer);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PreAuthenticationUserDetailsService preAuthenticationUserDetailsService) {
        final var provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(preAuthenticationUserDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
            @Qualifier("bearer-storage")
            TokenStorage accessTokenStorage,
            @Qualifier("cookie-storage")
            TokenStorage refreshTokenStorage,
            TokenService tokenService,
            UsersService usersService,
            JwtAuthenticationEntryPoint entryPoint
    ) {
        return new JwtAuthenticationConfigurer(accessTokenStorage, refreshTokenStorage, tokenService, usersService, entryPoint);
    }
}
