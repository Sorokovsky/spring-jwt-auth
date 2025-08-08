package org.sorokovsky.springsecurityjwtauth.factory;

import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.springframework.security.core.Authentication;

import java.util.function.Function;

public interface RefreshTokenFactory extends Function<Authentication, Token> {

}
