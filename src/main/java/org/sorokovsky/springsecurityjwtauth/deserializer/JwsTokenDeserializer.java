package org.sorokovsky.springsecurityjwtauth.deserializer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.sorokovsky.springsecurityjwtauth.exception.TokenNotParseException;
import org.sorokovsky.springsecurityjwtauth.exception.TokenNotValidException;

import java.text.ParseException;

@RequiredArgsConstructor
public class JwsTokenDeserializer extends JwtTokenDeserializer {
    private final JWSVerifier verifier;

    @Override
    public Token apply(String string) {
        try {
            final var signed = SignedJWT.parse(string);
            if (!signed.verify(verifier)) throw new TokenNotValidException();
            return extractFromClaims(signed.getJWTClaimsSet());
        } catch (JOSEException | ParseException exception) {
            throw new TokenNotParseException(exception.getMessage(), exception);
        }
    }
}
