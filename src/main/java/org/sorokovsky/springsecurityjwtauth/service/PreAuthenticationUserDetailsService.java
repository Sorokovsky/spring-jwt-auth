package org.sorokovsky.springsecurityjwtauth.service;

import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.Token;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final UsersService usersService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        final var principal = authenticationToken.getPrincipal();
        if (principal instanceof Token token) {
            return usersService.loadUserByUsername(token.email());
        }
        return null;
    }
}
