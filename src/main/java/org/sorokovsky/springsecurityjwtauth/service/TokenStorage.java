package org.sorokovsky.springsecurityjwtauth.service;

import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface TokenStorage {
    Optional<Token> get();
    void set(Token token);
    void clear();
}
