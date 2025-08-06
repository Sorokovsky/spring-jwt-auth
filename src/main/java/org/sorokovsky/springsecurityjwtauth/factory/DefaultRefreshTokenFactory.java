package org.sorokovsky.springsecurityjwtauth.factory;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DefaultRefreshTokenFactory implements RefreshTokenFactory {
    private Duration tokenLifetime = Duration.ofDays(7);

    @Override
    public Token apply(UserModel userModel) {
        final var now = Instant.now();
        final var expiresAt = now.plus(tokenLifetime);
        return new Token(UUID.randomUUID(), userModel.getEmail(), now, expiresAt);
    }
}
