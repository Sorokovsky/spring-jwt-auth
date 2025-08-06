package org.sorokovsky.springsecurityjwtauth.factory;


import org.sorokovsky.springsecurityjwtauth.contract.Token;

import java.util.function.Function;

public interface AccessTokenFactory extends Function<Token, Token> {
}
