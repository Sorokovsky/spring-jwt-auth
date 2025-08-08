package org.sorokovsky.springsecurityjwtauth.configuration;

import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.configurer.JwtAuthenticationConfigurer;
import org.sorokovsky.springsecurityjwtauth.configurer.RefreshTokenConfigurer;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.entrypoint.BearerAuthenticationEntryPoint;
import org.sorokovsky.springsecurityjwtauth.factory.AccessTokenFactory;
import org.sorokovsky.springsecurityjwtauth.factory.RefreshTokenFactory;
import org.sorokovsky.springsecurityjwtauth.serializer.TokenSerializer;
import org.sorokovsky.springsecurityjwtauth.service.AuthenticationService;
import org.sorokovsky.springsecurityjwtauth.service.PreAuthenticationUserDetailsService;
import org.sorokovsky.springsecurityjwtauth.service.TokenStorage;
import org.sorokovsky.springsecurityjwtauth.service.UsersService;
import org.sorokovsky.springsecurityjwtauth.strategy.JwtAuthenticationStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Qualifier("jws-deserializer")
            TokenDeserializer accessTokenDeserializer,
            @Qualifier("bearer-storage")
            TokenStorage accessTokenStorage,
            JwtAuthenticationStrategy jwtAuthenticationStrategy,
            @Qualifier("jwe-deserializer")
            TokenDeserializer refreshTokenDeserializer,
            PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider,
            @Qualifier("cookie-storage")
            TokenStorage refreshTokenStorage,
            @Qualifier("jws-serializer")
            TokenSerializer accessTokenSerializer,
            AccessTokenFactory accessTokenFactory,
            AuthenticationService authenticationService,
            UsersService usersService
    ) throws Exception {
        final var jwtAccessConfigurer = new JwtAuthenticationConfigurer();
        jwtAccessConfigurer.setAccessTokenDeserializer(accessTokenDeserializer);
        jwtAccessConfigurer.setAccessTokenStorage(accessTokenStorage);
        jwtAccessConfigurer.setAuthenticationEntryPoint(new BearerAuthenticationEntryPoint());
        jwtAccessConfigurer.setAuthenticationProvider(preAuthenticatedAuthenticationProvider);
        jwtAccessConfigurer.setRefreshTokenDeserializer(refreshTokenDeserializer);
        jwtAccessConfigurer.setRefreshTokenStorage(refreshTokenStorage);
        jwtAccessConfigurer.setAccessTokenSerializer(accessTokenSerializer);
        jwtAccessConfigurer.setAccessTokenFactory(accessTokenFactory);
        final var refreshTokensConfigurer = new RefreshTokenConfigurer();
        refreshTokensConfigurer.setRefreshTokenStorage(refreshTokenStorage);
        refreshTokensConfigurer.setAuthenticationService(authenticationService);
        refreshTokensConfigurer.setRefreshTokenDeserializer(refreshTokenDeserializer);
        refreshTokensConfigurer.setUsersService(usersService);
        http
                .authorizeHttpRequests(configuration -> configuration
                        .requestMatchers("/authentication/register", "/authentication/login").anonymous()
                        .requestMatchers("/authentication/get-me", "/authentication/logout").authenticated()
                        .requestMatchers("/v3/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(configuration -> configuration
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .addSessionAuthenticationStrategy(jwtAuthenticationStrategy)
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new CookieCsrfTokenRepository())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .sessionAuthenticationStrategy((_, _, _) -> {
                        })
                        .disable()
                );
        http.apply(jwtAccessConfigurer);
        http.apply(refreshTokensConfigurer);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider authenticationProvider(
            PreAuthenticationUserDetailsService preAuthenticationUserDetailsService) {
        final var provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(preAuthenticationUserDetailsService);
        return provider;
    }

    @Bean
    public JwtAuthenticationStrategy jwtAuthenticationStrategy(
            AccessTokenFactory accessTokenFactory,
            RefreshTokenFactory refreshTokenFactory,
            @Qualifier("jws-serializer")
            TokenSerializer accessTokenSerializer,
            @Qualifier("jwe-serializer")
            TokenSerializer refreshTokenSerializer,
            @Qualifier("bearer-storage")
            TokenStorage accessTokenStorage,
            @Qualifier("cookie-storage")
            TokenStorage refreshTokenStorage
    ) {
        return new JwtAuthenticationStrategy(
                accessTokenFactory,
                refreshTokenFactory,
                accessTokenSerializer,
                refreshTokenSerializer,
                accessTokenStorage,
                refreshTokenStorage);
    }
}