package org.sorokovsky.springsecurityjwtauth.factory;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.contract.Token;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DefaultAccessTokenFactory implements AccessTokenFactory {
    private Duration accessTokenLifetime = Duration.ofMinutes(15);

    @Override
    public Token apply(Token refreshToken) {
        final var now = Instant.now();
        final var expiresAt = now.plus(accessTokenLifetime);
        return new Token(UUID.randomUUID(), refreshToken.email(), now, expiresAt);
    }
}
