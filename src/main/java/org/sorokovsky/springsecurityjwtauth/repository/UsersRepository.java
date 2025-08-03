package org.sorokovsky.springsecurityjwtauth.repository;

import org.sorokovsky.springsecurityjwtauth.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Long id);
    List<UserEntity> findAll();
    UserEntity save(UserEntity user);
    void deleteById(Long id);
}
