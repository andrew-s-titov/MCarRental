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
    private String roleClaimName;
    @Value("${security.jwt.claim.type}")
    private String tokenTypeClaimName;

    @Override
    @NonNull
    public SecurityTokensDTO createTokens(@NonNull UserInfoDTO userInfo) {
        Assert.notNull(userInfo, DEFAULT_NULL_MESSAGE);

        var accessToken = tokenCreator.createAccessToken(userInfo);
        var refreshToken = tokenCreator.createRefreshToken(userInfo);

        return new SecurityTokensDTO(accessToken, refreshToken);
    }

    @Override
    @NonNull
    public UserInfoDTO userInfo(@NonNull JwtAuthenticationToken jwtAuthToken) {
        return getUserInfo(jwtAuthToken, TokenType.ACCESS);
    }

    @Override
    @NonNull
    public String refreshTokens(@NonNull JwtAuthenticationToken jwtAuthToken) {
        UserInfoDTO userInfo = getUserInfo(jwtAuthToken, TokenType.REFRESH);
        return tokenCreator.createAccessToken(userInfo);
    }

    @NonNull
    private UserInfoDTO getUserInfo(@NonNull JwtAuthenticationToken jwtAuthToken, TokenType tokenType) {
        Assert.notNull(jwtAuthToken, DEFAULT_NULL_MESSAGE);

        var jwt = jwtAuthToken.getToken();
        checkTokenType(jwt, tokenType);
        return new UserInfoDTO(retrieveUserId(jwt), retrieveRole(jwt));
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
