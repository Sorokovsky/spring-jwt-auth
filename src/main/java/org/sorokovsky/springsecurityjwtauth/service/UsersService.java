package org.sorokovsky.springsecurityjwtauth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.NewUserPayload;
import org.sorokovsky.springsecurityjwtauth.contract.UpdateUserPayload;
import org.sorokovsky.springsecurityjwtauth.exception.UserAlreadyExistsException;
import org.sorokovsky.springsecurityjwtauth.exception.UserNotFoundException;
import org.sorokovsky.springsecurityjwtauth.mapper.UserMapper;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;
import org.sorokovsky.springsecurityjwtauth.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsersService implements UserDetailsService {
    private final UsersRepository repository;
    private final UserMapper mapper;

    public Optional<UserModel> getById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    public Optional<UserModel> getByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toModel);
    }

    @Transactional(rollbackOn = UserAlreadyExistsException.class)
    public UserModel create(NewUserPayload payload) {
        final var candidate = getByEmail(payload.email());
        if (candidate.isPresent()) throw new UserAlreadyExistsException();
        return mapper.toModel(repository.save(mapper.toEntity(payload)));
    }

    @Transactional(rollbackOn = UserNotFoundException.class)
    public UserModel update(Long id, UpdateUserPayload payload) {
        final var oldState = getById(id).orElseThrow(UserNotFoundException::new);
        final var newState = mapper.toModel(payload);
        final var merged = mapper.merge(oldState, newState);
        return mapper.toModel(repository.save(mapper.toEntity(merged)));
    }

    @Transactional(rollbackOn = UserNotFoundException.class)
    public UserModel delete(Long id) {
        final var candidate = repository.findById(id).map(mapper::toModel).orElseThrow(UserNotFoundException::new);
        repository.deleteById(candidate.getId());
        return candidate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByEmail(username).orElseThrow(UserNotFoundException::new);
    }
}
