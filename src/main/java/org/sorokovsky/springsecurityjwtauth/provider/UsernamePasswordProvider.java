package org.sorokovsky.springsecurityjwtauth.provider;

import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.service.UsersService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernamePasswordProvider implements AuthenticationProvider {
    private final UsersService usersService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println(authentication);
        final var exception = new BadCredentialsException("Bad credentials");
        usersService.getByEmail(authentication.getName()).orElseThrow(() -> exception);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
