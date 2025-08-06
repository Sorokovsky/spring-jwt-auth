package org.sorokovsky.springsecurityjwtauth.serializer;

import org.sorokovsky.springsecurityjwtauth.contract.Token;

import java.util.function.Function;

public interface TokenSerializer extends Function<Token, String> {
}
