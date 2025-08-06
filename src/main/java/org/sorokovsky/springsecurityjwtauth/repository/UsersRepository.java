package org.sorokovsky.springsecurityjwtauth.repository;

import org.sorokovsky.springsecurityjwtauth.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByEmail(String email);

    UserEntity save(UserEntity user);

    void delete(UserEntity user);
}
