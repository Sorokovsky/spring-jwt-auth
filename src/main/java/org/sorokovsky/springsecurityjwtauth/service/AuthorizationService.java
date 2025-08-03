package org.sorokovsky.springsecurityjwtauth.service;

import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.user.CreateUser;
import org.sorokovsky.springsecurityjwtauth.contract.user.LoginPayload;
import org.sorokovsky.springsecurityjwtauth.entity.UserEntity;
import org.sorokovsky.springsecurityjwtauth.exception.InvalidPasswordException;
import org.sorokovsky.springsecurityjwtauth.exception.UserAlreadyExistsException;
import org.sorokovsky.springsecurityjwtauth.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final UsersService usersService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    @Qualifier("bearer-storage")
    private final TokenStorage bearerTokenStorage;
    @Qualifier("cookie-storage")
    private final TokenStorage cookieTokenStorage;

    public UserEntity register(CreateUser user) {
        final var candidate = usersService.findByEmail(user.email());
        if(candidate.isPresent()) {
            throw new UserAlreadyExistsException();
        } else {
            final var hashedPassword = passwordEncoder.encode(user.password());
            final var payload = new CreateUser(user.email(), hashedPassword, user.firstName(), user.lastName(), user.middleName());
            final var created = usersService.create(payload);
            authenticate(created);
            return created;
        }
    }

    public void logout() {
        bearerTokenStorage.clear();
        cookieTokenStorage.clear();
    }

    public UserEntity login(LoginPayload payload) {
        final var candidate = usersService.findByEmail(payload.email()).orElseThrow(UserNotFoundException::new);
        if(passwordEncoder.matches(payload.password(), candidate.getPassword())) {
            authenticate(candidate);
            return candidate;
        } else {
            throw new InvalidPasswordException();
        }
    }

    public boolean isAuthenticated() {
        final var accessToken = bearerTokenStorage.get().orElse(null);
        if(accessToken != null) return true;
        final var refreshToken = cookieTokenStorage.get().orElse(null);
        if(refreshToken == null) return false;
        final var userCandidate = usersService.findByEmail(refreshToken.email());
        if(userCandidate.isEmpty()) return false;
        authenticate(userCandidate.get());
        return true;
    }

    private void authenticate(UserEntity user) {
        final var accessToken = tokenService.generateAccessToken(user);
        final var refreshToken = tokenService.generateRefreshToken(user);
        bearerTokenStorage.set(accessToken);
        cookieTokenStorage.set(refreshToken);
    }
}
