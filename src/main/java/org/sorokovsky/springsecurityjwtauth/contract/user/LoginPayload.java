package org.sorokovsky.springsecurityjwtauth.contract.user;

public record LoginPayload(String email, String password) {
}
