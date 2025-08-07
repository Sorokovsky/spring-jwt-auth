package org.sorokovsky.springsecurityjwtauth.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sorokovsky.springsecurityjwtauth.contract.GetUserPayload;
import org.sorokovsky.springsecurityjwtauth.contract.NewUserPayload;
import org.sorokovsky.springsecurityjwtauth.contract.UpdateUserPayload;
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
    private GetUserPayload expectedGetUserPayload;

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

        expectedGetUserPayload = new GetUserPayload(
                expectedModel.getId(),
                expectedModel.getCreatedAt(),
                expectedModel.getUpdatedAt(),
                expectedModel.getEmail(),
                expectedModel.getFirstName(),
                expectedModel.getLastName(),
                expectedModel.getMiddleName()
        );
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

    @Test
    public void toGet_ShouldReturnCorrectPayload() {
        //when
        final var mappedPayload = mapper.toGet(expectedModel);


        //then
        assertEquals(expectedGetUserPayload, mappedPayload);
    }

    @Test
    public void toModel_ifFromUpdateUserPayload_shouldReturnCorrectPayload() {
        //given
        final var now = Date.from(Instant.now());
        final var model = new UserModel(null, null, "Andrey", null, null);
        model.setUpdatedAt(now);
        final var updatePayload = new UpdateUserPayload(null, null, "Andrey", null, null);

        //when
        final var mappedModel = mapper.toModel(updatePayload);
        mappedModel.setUpdatedAt(now);

        //then
        assertEquals(model, mappedModel);
    }

    @Test
    public void merge_ShouldReturnCorrectModel() {
        //given
        final var fromModel = new UserModel("Sorokovskys@ukr.net", "password", "Alex", "Sorokovsky", "Ivanovich");
        final var toModel = new UserModel(null, null, "Andrey", null, null);
        final var expectedResultModel = new UserModel("Sorokovskys@ukr.net", "password", "Andrey", "Sorokovsky", "Ivanovich");

        //when
        final var mappedResultModel = mapper.merge(fromModel, toModel);

        //then
        assertEquals(expectedResultModel, mappedResultModel);
    }
}
