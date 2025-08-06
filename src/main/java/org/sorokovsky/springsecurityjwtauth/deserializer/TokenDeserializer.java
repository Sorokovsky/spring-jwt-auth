package org.sorokovsky.springsecurityjwtauth.deserializer;

import org.sorokovsky.springsecurityjwtauth.contract.Token;

import java.util.function.Function;

public interface TokenDeserializer extends Function<String, Token> {
}
