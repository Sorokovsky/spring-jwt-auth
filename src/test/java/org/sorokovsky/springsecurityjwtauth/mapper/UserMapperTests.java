package org.sorokovsky.springsecurityjwtauth.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sorokovsky.springsecurityjwtauth.contract.NewUserPayload;
import org.sorokovsky.springsecurityjwtauth.entity.UserEntity;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserMapperTests {
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserMapper mapper;

    private UserEntity expectedEntity;
    private UserModel expectedModel;
    private NewUserPayload newUserPayload;

    @BeforeEach
    public void setUp() {
        final var now = Date.from(Instant.now());
        expectedModel = new UserModel(
                "Sorokovskys@ukr.net", "password", "Andrey",
                "Sorokovsky", "Ivanovich");
        expectedModel.setId(1L);
        expectedModel.setCreatedAt(now);
        expectedModel.setUpdatedAt(now);

        expectedEntity = new UserEntity(
                "Sorokovskys@ukr.net", "password", "Andrey",
                "Sorokovsky", "Ivanovich");
        expectedEntity.setCreatedAt(now);
        expectedEntity.setUpdatedAt(now);
        expectedEntity.setId(1L);

        newUserPayload = new NewUserPayload("Sorokovskys@ukr.net", "password", "Andrey",
                "Sorokovsky", "Ivanovich");
    }

    @Test
    public void toEntity_ifFromModel_shouldReturnCorrectEntity() {
        //when
        final var mappedEntity = mapper.toEntity(expectedModel);

        //then
        assertEquals(expectedEntity, mappedEntity);
    }

    @Test
    public void toEntity_ifFromNewUserPayload_shouldReturnCorrectEntity() {
        //given
        doReturn("password").when(passwordEncoder).encode("password");

        //when
        final var mappedEntity = mapper.toEntity(newUserPayload);
        mappedEntity.setId(expectedEntity.getId());
        mappedEntity.setCreatedAt(expectedEntity.getCreatedAt());
        mappedEntity.setUpdatedAt(expectedEntity.getUpdatedAt());
        //then
        assertEquals(expectedEntity, mappedEntity);
    }

    @Test
    public void toModel_shouldReturnCorrectModel() {
        //when
        final var mappedModel = mapper.toModel(expectedEntity);
        //then
        assertEquals(expectedModel, mappedModel);
    }
}
