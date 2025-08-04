package org.sorokovsky.springsecurityjwtauth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.service.AuthorizationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthorizationService authorizationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var authenticatedUser = authorizationService.isAuthenticated().orElse(null);
        if (authenticatedUser == null) {
            filterChain.doFilter(request, response);
            return;
        }
        final var authToken = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
        final var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }
}
