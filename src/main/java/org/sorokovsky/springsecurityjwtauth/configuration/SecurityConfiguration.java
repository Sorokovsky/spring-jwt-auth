package org.sorokovsky.springsecurityjwtauth.configuration;

import org.sorokovsky.springsecurityjwtauth.configurer.JwtAuthenticationConfigurer;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.entrypoint.BearerAuthenticationEntryPoint;
import org.sorokovsky.springsecurityjwtauth.service.PreAuthenticationUserDetailsService;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Qualifier("jws-deserializer")
            TokenDeserializer accessTokenDeserializer,
            @Qualifier("bearer-storage")
            TokenStorage accessTokenStorage
    ) throws Exception {
        final var jwtAccessConfigurer = new JwtAuthenticationConfigurer();
        jwtAccessConfigurer.setAccessTokenDeserializer(accessTokenDeserializer);
        jwtAccessConfigurer.setAccessTokenStorage(accessTokenStorage);
        jwtAccessConfigurer.setAuthenticationEntryPoint(new BearerAuthenticationEntryPoint());
        http
                .authorizeHttpRequests(configuration -> configuration
                        .requestMatchers("/authentication/register", "/authentication/login", "authentication/refresh-tokens").anonymous()
                        .requestMatchers("/authentication/get-me", "/authentication/logout").authenticated()
                        .requestMatchers("/v3/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new BearerAuthenticationEntryPoint())
                )
                .sessionManagement(configuration -> configuration
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(CsrfConfigurer::disable);
        http.apply(jwtAccessConfigurer);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PreAuthenticationUserDetailsService preAuthenticationUserDetailsService) {
        final var provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(preAuthenticationUserDetailsService);
        return provider;
    }
}