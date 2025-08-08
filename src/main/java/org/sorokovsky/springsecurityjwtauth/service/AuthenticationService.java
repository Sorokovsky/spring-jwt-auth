package org.sorokovsky.springsecurityjwtauth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sorokovsky.springsecurityjwtauth.contract.LoginPayload;
import org.sorokovsky.springsecurityjwtauth.contract.NewUserPayload;
import org.sorokovsky.springsecurityjwtauth.exception.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class AuthenticationService {

    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final TokenStorage accessTokenStorage;
    private final TokenStorage refreshTokenStorage;

    public AuthenticationService(
            UsersService usersService,
            PasswordEncoder passwordEncoder,
            HttpServletRequest request,
            HttpServletResponse response,
            SessionAuthenticationStrategy sessionAuthenticationStrategy,
            @Qualifier("bearer-storage")
            TokenStorage accessTokenStorage,
            @Qualifier("cookie-storage")
            TokenStorage refreshTokenStorage
    ) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
        this.response = response;
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
        this.accessTokenStorage = accessTokenStorage;
        this.refreshTokenStorage = refreshTokenStorage;
    }

    public void register(NewUserPayload newUser) {
        usersService.create(newUser);
        authenticate(newUser.email(), newUser.password());
    }

    public void login(LoginPayload payload) {
        final var invalidEmailException = new InvalidCredentialsException("Invalid email");
        final var invalidPasswordException = new InvalidCredentialsException("Invalid password");
        final var candidate = usersService.getByEmail(payload.email()).orElseThrow(() -> invalidEmailException);
        if (!passwordEncoder.matches(payload.password(), candidate.getPassword())) throw invalidPasswordException;
        authenticate(payload.email(), payload.password());
    }

    public void logout() {
        accessTokenStorage.clear();
        refreshTokenStorage.clear();
    }

    public void authenticate(String email, String password) {
        final var token = new UsernamePasswordAuthenticationToken(email, password);
        sessionAuthenticationStrategy.onAuthentication(token, request, response);
    }
}
