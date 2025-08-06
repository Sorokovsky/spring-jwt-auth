package org.sorokovsky.springsecurityjwtauth.service;

import java.util.Optional;

public interface TokenStorage {
    Optional<String> get();

    void set(String token);
    void clear();
}
