package org.sorokovsky.springsecurityjwtauth.service;

import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.user.CreateUser;
import org.sorokovsky.springsecurityjwtauth.entity.UserEntity;
import org.sorokovsky.springsecurityjwtauth.exception.UserAlreadyExistsException;
import org.sorokovsky.springsecurityjwtauth.mapper.UserMapper;
import org.sorokovsky.springsecurityjwtauth.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
    private final UsersRepository repository;
    private final UserMapper mapper;

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    public Optional<UserEntity> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public UserEntity create(CreateUser user) {
        final var candidate = repository.findByEmail(user.email());
        if (candidate.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        } else {
            final var entity = mapper.toUserEntity(user);
            return repository.save(entity);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
