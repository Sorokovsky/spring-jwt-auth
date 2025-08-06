package org.sorokovsky.springsecurityjwtauth.deserializer;

import com.nimbusds.jwt.JWTClaimsSet;
import org.sorokovsky.springsecurityjwtauth.contract.Token;

import java.util.UUID;

public abstract class JwtTokenDeserializer implements TokenDeserializer {
    protected Token extractFromClaims(JWTClaimsSet claimsSet) {
        final var createdAt = claimsSet.getIssueTime().toInstant();
        final var expiresAt = claimsSet.getExpirationTime().toInstant();
        return new Token(UUID.fromString(claimsSet.getJWTID()), claimsSet.getSubject(), createdAt, expiresAt);
    }
}
