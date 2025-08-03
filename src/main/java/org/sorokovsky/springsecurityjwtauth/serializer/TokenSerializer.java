package org.sorokovsky.springsecurityjwtauth.serializer;

import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public interface TokenSerializer extends Function<Token, String> {
}
