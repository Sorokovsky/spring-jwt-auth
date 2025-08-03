package org.sorokovsky.springsecurityjwtauth.mapper;

import org.sorokovsky.springsecurityjwtauth.contract.user.CreateUser;
import org.sorokovsky.springsecurityjwtauth.contract.user.GetUser;
import org.sorokovsky.springsecurityjwtauth.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {
    public GetUser toGetUser(UserEntity entity) {
        return new GetUser(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getMiddleName()
        );
    }

    public UserEntity toUserEntity(CreateUser user) {
        return new UserEntity(user.email(), user.password(), user.firstName(), user.lastName(), user.middleName(), List.of());
    }
}
