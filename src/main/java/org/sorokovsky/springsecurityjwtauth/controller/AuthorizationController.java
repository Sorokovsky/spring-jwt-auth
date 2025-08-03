package org.sorokovsky.springsecurityjwtauth.controller;

import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.user.CreateUser;
import org.sorokovsky.springsecurityjwtauth.contract.user.GetUser;
import org.sorokovsky.springsecurityjwtauth.contract.user.LoginPayload;
import org.sorokovsky.springsecurityjwtauth.entity.UserEntity;
import org.sorokovsky.springsecurityjwtauth.mapper.UserMapper;
import org.sorokovsky.springsecurityjwtauth.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("authorization")
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationService service;
    private final UserMapper mapper;

    @PostMapping("register")
    public ResponseEntity<GetUser> register(@RequestBody CreateUser user, UriComponentsBuilder uriBuilder) {
        final var createdUser = mapper.toGetUser(service.register(user));
        final var uri = uriBuilder.pathSegment("/me").build().toUri();
        return ResponseEntity.created(uri).body(createdUser);
    }

    @PutMapping("login")
    public ResponseEntity<GetUser> login(@RequestBody LoginPayload payload) {
        return ResponseEntity.ok(mapper.toGetUser(service.login(payload)));
    }

    @DeleteMapping("logout")
    public ResponseEntity<Void> logout() {
        service.logout();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("me")
    public ResponseEntity<GetUser> getMe(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(mapper.toGetUser(user));
    }
}
