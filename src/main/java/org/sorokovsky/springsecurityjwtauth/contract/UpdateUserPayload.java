package org.sorokovsky.springsecurityjwtauth.contract;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserPayload(
        @NotNull
        @Email
        String email,
        @NotNull
        @Size(min = 6)
        String password,
        String firstName,
        String lastName,
        String middleName
) {
}
