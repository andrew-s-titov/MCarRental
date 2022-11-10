package com.mcarrental.jwtauthservice.service.impl;

import com.mcarrental.TestConfig;
import com.mcarrental.jwtauthservice.config.JwtConfig;
import com.mcarrental.jwtauthservice.security.TokenType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.Instant;

import static com.mcarrental.TestVars.TEST_ID;
import static com.mcarrental.TestVars.TEST_ROLE;
import static com.mcarrental.TestVars.USER_INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.yaml")
@Import({TestConfig.class, JwtTokenCreatorImpl.class, JwtConfig.class})
public class JwtTokenCreatorImplTest {

    @Autowired
    private JwtTokenCreatorImpl testTokenCreator;
    @Autowired
    private JwtDecoder jwtDecoder;

    @Value("${security.jwt.claim.type}")
    private String tokenTypeClaim;
    @Value("${security.jwt.claim.role}")
    private String tokenRoleClaim;
    @Value("${security.jwt.expiry.access-token}")
    private Integer accessTokenExpiryHours;
    @Value("${security.jwt.expiry.refresh-token}")
    private Integer refreshTokenExpiryHours;

    @Test
    @DisplayName("createAccessToken() - should return not blank JWT string with correct claims and expiration period")
    public void createAccessTokenTest() {
        // when
        String accessToken = testTokenCreator.createAccessToken(USER_INFO);

        // then
        assertTrue(StringUtils.isNotBlank(accessToken), "method returns blank string instead of a token");
        String[] tokenParts = accessToken.split("\\.");
        assertEquals(3, tokenParts.length, "unexpected number of jwt parts");

        Jwt decodedJwt = jwtDecoder.decode(accessToken);
        assertEquals(TokenType.ACCESS.toString(), decodedJwt.getClaim(tokenTypeClaim),
                "wrong or missing token type in encoded token");
        assertClaimsAreCorrect(decodedJwt);
        assertExpirationIsCorrect(decodedJwt, accessTokenExpiryHours);
    }

    @Test
    @DisplayName("createRefreshToken() - should return not blank JWT string with correct claims and expiration period")
    public void createRefreshTokenTest() {
        // when
        String refreshToken = testTokenCreator.createRefreshToken(USER_INFO);

        // then
        assertTrue(StringUtils.isNotBlank(refreshToken), "method returns blank string instead of a token");
        String[] tokenParts = refreshToken.split("\\.");
        assertEquals(3, tokenParts.length, "unexpected number of jwt parts");

        Jwt decodedJwt = jwtDecoder.decode(refreshToken);
        assertEquals(TokenType.REFRESH.toString(), decodedJwt.getClaim(tokenTypeClaim),
                "wrong or missing token type in encoded token");
        assertClaimsAreCorrect(decodedJwt);
        assertExpirationIsCorrect(decodedJwt, refreshTokenExpiryHours);
    }

    private void assertClaimsAreCorrect(Jwt jwt) {
        assertEquals(TEST_ID.toString(), jwt.getSubject(),
                "wrong or missing subject (user id) in encoded token");
        assertEquals(TEST_ROLE.toString(), jwt.getClaim(tokenRoleClaim),
                "wrong or missing user role in encoded token");
    }

    private void assertExpirationIsCorrect(Jwt jwt, int expiryHours) {
        Instant issuedAt = jwt.getIssuedAt();
        Instant expiresAt = jwt.getExpiresAt();
        if (issuedAt != null) {
            assertEquals(Duration.ofHours(expiryHours), Duration.between(issuedAt, expiresAt));
        }
    }
}
