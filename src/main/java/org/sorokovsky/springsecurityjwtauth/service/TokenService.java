package org.sorokovsky.springsecurityjwtauth.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.sorokovsky.springsecurityjwtauth.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Setter
@RequiredArgsConstructor
public class TokenService {
    private Duration accessTokenLifetime = Duration.ofMinutes(15);
    private Duration refreshTokenLifetime = Duration.ofDays(7);

    public Token generateAccessToken(UserEntity user) {
        return generateToken(user, accessTokenLifetime);
    }

    public Token generateRefreshToken(UserEntity user) {
        return generateToken(user, refreshTokenLifetime);
    }

    private Token generateToken(UserEntity user, Duration lifetime) {
        final var now = Instant.now();
        final var expirationTime = now.plus(lifetime);
        return new Token(UUID.randomUUID(), user.getEmail(), user.getRoles().stream().map(string -> "ROLE_" + string).toList(), now, expirationTime);
    }
}
