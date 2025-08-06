package org.sorokovsky.springsecurityjwtauth.serializer;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.sorokovsky.springsecurityjwtauth.exception.TokenNotSerializedException;

@RequiredArgsConstructor
@Setter
public class JweTokenSerializer extends JwtTokenSerializer {
    private final JWEEncrypter encrypter;
    private EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;
    private JWEAlgorithm algorithm = JWEAlgorithm.A192KW;

    @Override
    public String apply(Token token) {
        final var header = new JWEHeader.Builder(algorithm, encryptionMethod)
                .keyID(token.id().toString())
                .build();
        try {
            final var encrypted = new EncryptedJWT(header, buildClaims(token));
            encrypted.encrypt(encrypter);
            return encrypted.serialize();
        } catch (JOSEException exception) {
            throw new TokenNotSerializedException(exception.getMessage(), exception);
        }
    }
}
