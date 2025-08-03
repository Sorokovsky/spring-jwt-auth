package org.sorokovsky.springsecurityjwtauth.contract.user;

import java.util.Date;

public record GetUser(
        Long id,
        Date createdAt,
        Date updatedAt,
        String email,
        String firstName,
        String lastName,
        String middleName
) {
}
