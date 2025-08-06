package org.sorokovsky.springsecurityjwtauth.factory;

import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;

import java.util.function.Function;

public interface RefreshTokenFactory extends Function<UserModel, Token> {

}
