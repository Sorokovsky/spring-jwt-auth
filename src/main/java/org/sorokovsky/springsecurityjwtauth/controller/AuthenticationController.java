package org.sorokovsky.springsecurityjwtauth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.GetUserPayload;
import org.sorokovsky.springsecurityjwtauth.contract.LoginPayload;
import org.sorokovsky.springsecurityjwtauth.contract.NewUserPayload;
import org.sorokovsky.springsecurityjwtauth.mapper.UserMapper;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;
import org.sorokovsky.springsecurityjwtauth.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("authentication")
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserMapper mapper;

    @GetMapping("get-me")
    public ResponseEntity<GetUserPayload> getMe(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(mapper.toGet(user));
    }

    @PostMapping("register")
    public ResponseEntity<GetUserPayload> register(@Valid @RequestBody NewUserPayload newUser, UriComponentsBuilder uriBuilder) {
        return ResponseEntity
                .created(uriBuilder.pathSegment("authentication/me").build().toUri())
                .body(mapper.toGet(service.register(newUser)));
    }

    @PutMapping("login")
    public ResponseEntity<GetUserPayload> login(@Valid @RequestBody LoginPayload payload) {
        return ResponseEntity.ok(mapper.toGet(service.login(payload)));
    }

    @DeleteMapping("logout")
    public ResponseEntity<GetUserPayload> logout(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(mapper.toGet(service.logout(user)));
    }

    @PutMapping("refresh-tokens")
    public ResponseEntity<GetUserPayload> refreshTokens() {
        return ResponseEntity.ok(mapper.toGet(service.refreshTokens()));
    }
}
