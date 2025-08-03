package org.sorokovsky.springsecurityjwtauth.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.sorokovsky.springsecurityjwtauth.serializer.TokenSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;

@Configuration
public class TokenConfig {
    @Bean
    public JWSVerifier jwsVerifier(@Value("${token.secret-key:secretKey}") String secretKey) throws ParseException, JOSEException {
        return new MACVerifier(OctetSequenceKey.parse(secretKey));
    }

    @Bean
    public JWSSigner jwsSigner(@Value("${token.secret-key:secretKey}") String secretKey) throws JOSEException, ParseException {
        return new MACSigner(OctetSequenceKey.parse(secretKey));
    }
}
