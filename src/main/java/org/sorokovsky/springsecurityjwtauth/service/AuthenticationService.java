package org.sorokovsky.springsecurityjwtauth.service;

import org.sorokovsky.springsecurityjwtauth.contract.LoginPayload;
import org.sorokovsky.springsecurityjwtauth.contract.NewUserPayload;
import org.sorokovsky.springsecurityjwtauth.exception.InvalidCredentialsException;
import org.sorokovsky.springsecurityjwtauth.factory.AccessTokenFactory;
import org.sorokovsky.springsecurityjwtauth.factory.RefreshTokenFactory;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;
import org.sorokovsky.springsecurityjwtauth.serializer.TokenSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final TokenSerializer accessTokenSerializer;
    private final TokenSerializer refreshTokenSerializer;
    private final AccessTokenFactory accessTokenFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final TokenStorage accessTokenStorage;
    private final TokenStorage refreshTokenStorage;

    public AuthenticationService(
            @Qualifier("jws-serializer")
            TokenSerializer accessTokenSerializer,
            @Qualifier("jwe-serializer")
            TokenSerializer refreshTokenSerializer,
            AccessTokenFactory accessTokenFactory,
            RefreshTokenFactory refreshTokenFactory,
            UsersService usersService,
            PasswordEncoder passwordEncoder,
            @Qualifier("bearer-storage")
            TokenStorage accessTokenStorage,
            @Qualifier("cookie-storage")
            TokenStorage refreshTokenStorage
    ) {
        this.accessTokenSerializer = accessTokenSerializer;
        this.refreshTokenSerializer = refreshTokenSerializer;
        this.accessTokenFactory = accessTokenFactory;
        this.refreshTokenFactory = refreshTokenFactory;
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenStorage = accessTokenStorage;
        this.refreshTokenStorage = refreshTokenStorage;
    }

    public UserModel register(NewUserPayload newUser) {
        final var created = usersService.create(newUser);
        authenticate(created);
        return created;
    }

    public UserModel login(LoginPayload payload) {
        final var exception = new InvalidCredentialsException();
        final var candidate = usersService.getByEmail(payload.email()).orElseThrow(() -> exception);
        if (!passwordEncoder.matches(payload.password(), candidate.getPassword())) throw exception;
        authenticate(candidate);
        return candidate;
    }

    public UserModel logout(@AuthenticationPrincipal UserModel user) {
        accessTokenStorage.clear();
        refreshTokenStorage.clear();
        return user;
    }

    private void authenticate(UserModel user) {
        final var refreshToken = refreshTokenFactory.apply(user);
        final var accessToken = accessTokenFactory.apply(refreshToken);
        accessTokenStorage.set(accessTokenSerializer.apply(accessToken));
        refreshTokenStorage.set(refreshTokenSerializer.apply(refreshToken));
    }
}
