package org.sorokovsky.springsecurityjwtauth.configuration;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.sorokovsky.springsecurityjwtauth.deserializer.JweTokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.deserializer.JwsTokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.deserializer.TokenDeserializer;
import org.sorokovsky.springsecurityjwtauth.factory.AccessTokenFactory;
import org.sorokovsky.springsecurityjwtauth.factory.DefaultAccessTokenFactory;
import org.sorokovsky.springsecurityjwtauth.factory.DefaultRefreshTokenFactory;
import org.sorokovsky.springsecurityjwtauth.factory.RefreshTokenFactory;
import org.sorokovsky.springsecurityjwtauth.serializer.JweTokenSerializer;
import org.sorokovsky.springsecurityjwtauth.serializer.JwsTokenSerializer;
import org.sorokovsky.springsecurityjwtauth.serializer.TokenSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.time.Duration;

@Configuration
public class TokensConfiguration {
    @Bean
    @Qualifier("jws-serializer")
    public TokenSerializer jwsTokenSerializer(JWSSigner signer) {
        return new JwsTokenSerializer(signer);
    }

    @Bean
    @Qualifier("jws-deserializer")
    public TokenDeserializer jwsTokenDeserializer(JWSVerifier verifier) {
        return new JwsTokenDeserializer(verifier);
    }

    @Bean
    @Qualifier("jwe-serializer")
    public TokenSerializer jweTokenSerializer(JWEEncrypter encrypter) {
        return new JweTokenSerializer(encrypter);
    }

    @Bean
    @Qualifier("jwe-deserializer")
    public TokenDeserializer jweTokenDeserializer(JWEDecrypter decrypter) {
        return new JweTokenDeserializer(decrypter);
    }

    @Bean
    public AccessTokenFactory accessTokenFactory(@Qualifier("access-lifetime") Duration lifetime) {
        return new DefaultAccessTokenFactory(lifetime);
    }

    @Bean
    public RefreshTokenFactory refreshTokenFactory(@Qualifier("refresh-lifetime") Duration lifetime) {
        return new DefaultRefreshTokenFactory(lifetime);
    }

    @Bean
    public JWSSigner jwsSigner(@Value("${token.signing-key}") String signingKey) throws ParseException, KeyLengthException {
        return new MACSigner(OctetSequenceKey.parse(signingKey));
    }

    @Bean
    public JWSVerifier jwsVerifier(@Value("${token.signing-key}") String signingKey) throws ParseException, JOSEException {
        return new MACVerifier(OctetSequenceKey.parse(signingKey));
    }

    @Bean
    public JWEEncrypter jweEncrypter(@Value("${token.encryption-key}") String encryptionKey) throws ParseException, JOSEException {
        return new DirectEncrypter(OctetSequenceKey.parse(encryptionKey));
    }

    @Bean
    public JWEDecrypter jweDecrypter(@Value("${token.encryption-key}") String encryptionKey) throws ParseException, JOSEException {
        return new DirectDecrypter(OctetSequenceKey.parse(encryptionKey));
    }

    @Bean
    @Qualifier("access-lifetime")
    public Duration accessTokenLifetime() {
        return Duration.ofMinutes(15);
    }

    @Bean
    @Qualifier("refresh-lifetime")
    public Duration refreshTokenLifetime() {
        return Duration.ofDays(7);
    }
}
