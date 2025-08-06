package org.sorokovsky.springsecurityjwtauth.mapper;

import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.GetUserPayload;
import org.sorokovsky.springsecurityjwtauth.contract.NewUserPayload;
import org.sorokovsky.springsecurityjwtauth.contract.UpdateUserPayload;
import org.sorokovsky.springsecurityjwtauth.entity.UserEntity;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserModel toModel(UserEntity entity) {
        final var model = new UserModel(
                entity.getEmail(), entity.getPassword(),
                entity.getFirstName(), entity.getLastName(), entity.getMiddleName());
        model.setId(entity.getId());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    public UserEntity toEntity(UserModel model) {
        final var entity = new UserEntity(
                model.getEmail(), model.getPassword(),
                model.getFirstName(), model.getLastName(), model.getMiddleName());
        entity.setId(model.getId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }

    public UserEntity toEntity(NewUserPayload payload) {
        final var entity = new UserEntity(
                payload.email(), passwordEncoder.encode(payload.password()), payload.firstName(), payload.lastName(), payload.middleName());
        final var now = Date.from(Instant.now());
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
    }

    public UserModel toModel(UpdateUserPayload payload) {
        final var model = new UserModel(payload.email(), passwordEncoder.encode(payload.password()), payload.firstName(), payload.lastName(), payload.middleName());
        model.setUpdatedAt(Date.from(Instant.now()));
        return model;
    }

    public UserModel merge(UserModel oldModel, UserModel newModel) {
        final var merged = new UserModel();
        merged.setEmail(chooseString(oldModel.getEmail(), newModel.getEmail()));
        merged.setPassword(chooseString(oldModel.getPassword(), newModel.getPassword()));
        merged.setFirstName(chooseString(oldModel.getFirstName(), newModel.getFirstName()));
        merged.setLastName(chooseString(oldModel.getLastName(), newModel.getLastName()));
        merged.setMiddleName(chooseString(oldModel.getMiddleName(), newModel.getMiddleName()));
        merged.setId(oldModel.getId());
        merged.setCreatedAt(oldModel.getCreatedAt());
        merged.setUpdatedAt(newModel.getUpdatedAt());
        return merged;
    }

    private String chooseString(String oldString, String newString) {
        if (newString != null && !newString.isBlank()) {
            return newString;
        } else {
            return oldString;
        }
    }

    public GetUserPayload toGet(UserModel user) {
        return new GetUserPayload(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getMiddleName());
    }
}