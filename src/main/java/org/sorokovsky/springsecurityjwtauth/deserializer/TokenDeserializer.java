package org.sorokovsky.springsecurityjwtauth.deserializer;

import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public interface TokenDeserializer extends Function<String, Token> {
}
