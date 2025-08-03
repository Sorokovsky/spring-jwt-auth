package org.sorokovsky.springsecurityjwtauth.controller;

import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.user.CreateUser;
import org.sorokovsky.springsecurityjwtauth.contract.user.GetUser;
import org.sorokovsky.springsecurityjwtauth.mapper.UserMapper;
import org.sorokovsky.springsecurityjwtauth.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService service;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<GetUser> create(@RequestBody CreateUser user, UriComponentsBuilder uriBuilder) {
        final var created = service.create(user);
        final var uri = uriBuilder.path("/users/%d".formatted(created.getId())).build().toUri();
        return ResponseEntity.created(uri).body(mapper.toGetUser(created));
    }

    @GetMapping
    public ResponseEntity<List<GetUser>> getAll() {
        return ResponseEntity.ok(service.findAll()
                .stream()
                .map(mapper::toGetUser).toList()
        );
    }
}