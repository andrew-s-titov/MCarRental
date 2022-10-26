package com.mcarrental.jwtauthservice.service.impl;

import com.mcarrental.jwtauthservice.dto.SecurityTokensDTO;
import com.mcarrental.jwtauthservice.dto.UserInfoDTO;
import com.mcarrental.jwtauthservice.exception.InvalidRequestException;
import com.mcarrental.jwtauthservice.security.Role;
import com.mcarrental.jwtauthservice.security.TokenType;
import com.mcarrental.jwtauthservice.service.TokenCreator;
import com.mcarrental.jwtauthservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class JwtTokenServiceImpl implements TokenService {

    private static final String DEFAULT_NULL_MESSAGE = "Null argument passed";

    private final TokenCreator tokenCreator;

    @Value("${security.jwt.claim.role}")
    private String roleClaimName = "role";
    @Value("${security.jwt.claim.type}")
    private String tokenTypeClaimName = "type";

    @Override
    @NonNull
    public SecurityTokensDTO createTokens(@NonNull UserInfoDTO userInfo) {
        Assert.notNull(userInfo, DEFAULT_NULL_MESSAGE);

        String accessToken = tokenCreator.createAccessToken(userInfo.getUserId(), userInfo.getUserRole());
        String refreshToken = tokenCreator.createRefreshToken(userInfo.getUserId(), userInfo.getUserRole());

        return new SecurityTokensDTO(accessToken, refreshToken);
    }

    @Override
    @NonNull
    public UserInfoDTO userInfo(@NonNull JwtAuthenticationToken jwtAuthToken) {
        Assert.notNull(jwtAuthToken, DEFAULT_NULL_MESSAGE);

        Jwt token = jwtAuthToken.getToken();
        checkTokenType(token, TokenType.ACCESS);

        return new UserInfoDTO(retrieveUserId(token), retrieveRole(token));
    }

    @Override
    @NonNull
    public String refreshTokens(@NonNull JwtAuthenticationToken jwtAuthToken) {
        Assert.notNull(jwtAuthToken, DEFAULT_NULL_MESSAGE);

        Jwt token = jwtAuthToken.getToken();
        checkTokenType(token, TokenType.REFRESH);

        return tokenCreator.createAccessToken(retrieveUserId(token), retrieveRole(token));
    }

    private void checkTokenType(Jwt jwt, TokenType expectedType) {
        if (!expectedType.equals(TokenType.valueOf(jwt.getClaim(tokenTypeClaimName)))) {
            throw new InvalidRequestException("Wrong token type");
        }
    }

    private Role retrieveRole(Jwt token) {
        return Role.valueOf(token.getClaim(roleClaimName));
    }

    private UUID retrieveUserId(Jwt token) {
        return UUID.fromString(token.getSubject());
    }
}
