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
    public ResponseEntity<Void> register(@Valid @RequestBody NewUserPayload newUser, UriComponentsBuilder uriBuilder) {
        service.register(newUser);
        return ResponseEntity
                .created(uriBuilder.fragment("/me").build().toUri()).build();
    }

    @PutMapping("login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginPayload payload) {
        service.login(payload);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("logout")
    public ResponseEntity<Void> logout() {
        service.logout();
        return ResponseEntity.noContent().build();
    }
}
