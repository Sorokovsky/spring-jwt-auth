package org.sorokovsky.springsecurityjwtauth.contract;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserPayload(
        @NotNull(message = "{errors.not-null}") @Email(message = "{errors.invalid-email-format}") String email,
        @NotNull(message = "{errors.not-null}") @Size(min = 6, message = "{errors.invalid-password-size}") String password,
        String firstName,
        String lastName,
        String middleName
) {
}
