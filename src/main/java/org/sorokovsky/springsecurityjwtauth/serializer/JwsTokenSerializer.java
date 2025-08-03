package org.sorokovsky.springsecurityjwtauth.serializer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.sorokovsky.springsecurityjwtauth.exception.TokenNotSerializedException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Primary
@RequiredArgsConstructor
@Setter
@Component
public class JwsTokenSerializer implements TokenSerializer {
    private final JWSSigner signer;
    private JWSAlgorithm algorithm = JWSAlgorithm.HS256;

    @Override
    public String apply(Token token) {
        final var header = new JWSHeader.Builder(algorithm).keyID(token.id().toString()).build();
        final var claims = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.email())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", String.join(",", token.authorities()))
                .build();
        final var signed = new SignedJWT(header, claims);
        try {
            signed.sign(signer);
            return signed.serialize();
        } catch (JOSEException exception) {
            throw new TokenNotSerializedException(exception.getMessage());
        }
    }
}
