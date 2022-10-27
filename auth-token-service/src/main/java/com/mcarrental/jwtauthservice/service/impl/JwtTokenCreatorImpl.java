package com.mcarrental.jwtauthservice.service.impl;

import com.mcarrental.jwtauthservice.dto.UserInfoDTO;
import com.mcarrental.jwtauthservice.security.TokenType;
import com.mcarrental.jwtauthservice.service.TokenCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class JwtTokenCreatorImpl implements TokenCreator {

    private final JwtEncoder jwtEncoder;

    @Value("${security.jwt.expiry.access-token}")
    private Integer accessTokenExpiryHours;
    @Value("${security.jwt.expiry.refresh-token}")
    private Integer refreshTokenExpiryHours;
    @Value("${security.jwt.claim.role}")
    private String roleClaimName = "role";
    @Value("${security.jwt.claim.type}")
    private String tokenTypeClaimName = "type";

    @Override
    public String createAccessToken(UserInfoDTO userInfo) {
        return createJwt(userInfo, accessTokenExpiryHours, TokenType.ACCESS);
    }

    @Override
    public String createRefreshToken(UserInfoDTO userInfo) {
        return createJwt(userInfo, refreshTokenExpiryHours, TokenType.REFRESH);
    }

    private String createJwt(UserInfoDTO userInfo, Integer expiryHours, TokenType tokenType) {
        var userId = userInfo.getUserId();
        var userRole = userInfo.getUserRole();
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(expiryHours, ChronoUnit.HOURS))
                .subject(userId.toString())
                .claim(roleClaimName, userRole)
                .claim(tokenTypeClaimName, tokenType)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
