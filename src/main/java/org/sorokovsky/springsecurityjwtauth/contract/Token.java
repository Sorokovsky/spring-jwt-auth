package org.sorokovsky.springsecurityjwtauth.contract;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Token(UUID id, String email, List<String> authorities, Instant createdAt, Instant expiresAt) {
}
