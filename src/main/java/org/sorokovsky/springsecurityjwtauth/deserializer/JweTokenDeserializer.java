package org.sorokovsky.springsecurityjwtauth.deserializer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.sorokovsky.springsecurityjwtauth.exception.TokenNotParseException;

import java.text.ParseException;

@RequiredArgsConstructor
public class JweTokenDeserializer extends JwtTokenDeserializer {
    private final JWEDecrypter decrypter;

    @Override
    public Token apply(String string) {
        try {
            final var decrypted = EncryptedJWT.parse(string);
            decrypted.decrypt(decrypter);
            return extractFromClaims(decrypted.getJWTClaimsSet());
        } catch (ParseException | JOSEException exception) {
            throw new TokenNotParseException(exception.getMessage(), exception);
        }
    }
}
