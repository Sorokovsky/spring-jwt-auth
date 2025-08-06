package org.sorokovsky.springsecurityjwtauth.service;

import lombok.RequiredArgsConstructor;
import org.sorokovsky.springsecurityjwtauth.contract.NewUserPayload;
import org.sorokovsky.springsecurityjwtauth.factory.AccessTokenFactory;
import org.sorokovsky.springsecurityjwtauth.factory.RefreshTokenFactory;
import org.sorokovsky.springsecurityjwtauth.model.UserModel;
import org.sorokovsky.springsecurityjwtauth.serializer.TokenSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Qualifier("jws-serializer")
    private final TokenSerializer accessTokenSerializer;
    @Qualifier("jwe-serializer")
    private final TokenSerializer refreshTokenSerializer;
    private final AccessTokenFactory accessTokenFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final UsersService usersService;

    public UserModel register(NewUserPayload newUser) {
        return null;
    }
}
