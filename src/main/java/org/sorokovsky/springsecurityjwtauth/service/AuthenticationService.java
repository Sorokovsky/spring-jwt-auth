package org.sorokovsky.springsecurityjwtauth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.LoginPayload;
import org.sorokovsky.springsecurityjwtauth.contract.NewUserPayload;
import org.sorokovsky.springsecurityjwtauth.exception.InvalidCredentialsException;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequiredArgsConstructor
@RequestScope
public class AuthenticationService {
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public void register(NewUserPayload newUser) {
        final var created = usersService.create(newUser);
        authenticate(created);
    }

    public void login(LoginPayload payload) {
        final var invalidEmailException = new InvalidCredentialsException("Invalid email");
        final var invalidPasswordException = new InvalidCredentialsException("Invalid password");
        final var candidate = usersService.getByEmail(payload.email()).orElseThrow(() -> invalidEmailException);
        if (!passwordEncoder.matches(payload.password(), candidate.getPassword())) throw invalidPasswordException;
        authenticate(candidate);
    }

    public void logout(UserModel user) {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(user.getEmail(), user.getPassword()));
    }

    private void authenticate(UserModel user) {
        final var requestToken = UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(requestToken);
        sessionAuthenticationStrategy.onAuthentication(requestToken, request, response);
    }
}
