package org.sorokovsky.springsecurityjwtauth.contract;

import java.util.Date;

public record GetUserPayload(Long id, Date createdAt, Date updatedAt, String email, String firstName, String lastName,
                             String middleName) {
}
