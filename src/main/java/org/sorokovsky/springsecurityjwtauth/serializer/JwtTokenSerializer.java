package org.sorokovsky.springsecurityjwtauth.serializer;

import com.nimbusds.jwt.JWTClaimsSet;
import org.sorokovsky.springsecurityjwtauth.contract.Token;

import java.util.Date;

public abstract class JwtTokenSerializer implements TokenSerializer {

    protected JWTClaimsSet buildClaims(Token token) {
        return new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .subject(token.email())
                .build();
    }
}
