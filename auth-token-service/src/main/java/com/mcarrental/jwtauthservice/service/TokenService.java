package com.mcarrental.jwtauthservice.service;

import com.mcarrental.jwtauthservice.dto.SecurityTokensDTO;
import com.mcarrental.jwtauthservice.dto.UserInfoDTO;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public interface TokenService {

    @NonNull
    SecurityTokensDTO createTokens(@NonNull UserInfoDTO userInfo);

    @NonNull
    UserInfoDTO userInfo(@NonNull JwtAuthenticationToken jwtAuthenticationToken);

    @NonNull
    String refreshTokens(@NonNull JwtAuthenticationToken jwtAuthenticationToken);
}