package org.sorokovsky.springsecurityjwtauth.deserializer;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.sorokovsky.springsecurityjwtauth.exception.TokenNotParsedException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
@Setter
@Component
public class JwsTokenDeserializer implements TokenDeserializer {
    private final JWSVerifier verifier;

    @Override
    public Token apply(String string) {
        try {
            final var signed = SignedJWT.parse(string);
            if(!signed.verify(verifier)) {
                throw new TokenNotParsedException("Token invalid");
            } else {
                final var claims = signed.getJWTClaimsSet();
                final var authorities = Arrays.stream(claims.getStringClaim("authorities").split(",")).toList();
                final var startTime = claims.getIssueTime().toInstant();
                final var endTime = claims.getExpirationTime().toInstant();
                return new Token(UUID.fromString(claims.getJWTID()), claims.getSubject(), authorities, startTime, endTime);
            }
        } catch (ParseException | JOSEException exception) {
            throw new TokenNotParsedException(exception.getMessage());
        }
    }
}
