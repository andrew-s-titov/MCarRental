package com.mcarrental.jwtauthservice.service;

import com.mcarrental.jwtauthservice.security.Role;

import java.util.UUID;

public interface TokenCreator {

    String createAccessToken(UUID userId, Role userRole);

    String createRefreshToken(UUID userId, Role userRole);
}
