package com.mcarrental.jwtauthservice.config;

import com.mcarrental.TestConfig;
import com.mcarrental.jwtauthservice.security.TokenType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.mcarrental.TestVars.TEST_ID;
import static com.mcarrental.TestVars.TEST_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.yaml")
@Import({TestConfig.class, JwtConfig.class})
public class JwtConfigTest {

    private static final Instant NOW = Instant.now();
    private static final Instant EXPIRATION = NOW.plus(3, ChronoUnit.HOURS);
    private static final TokenType EXPECTED_TOKEN_TYPE = TokenType.ACCESS;

    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;

    @Value("${security.jwt.claim.type}")
    private String tokenTypeClaim;
    @Value("${security.jwt.claim.role}")
    private String tokenRoleClaim;

    @Test
    @DisplayName("check if encoder and decoder are created and works properly")
    public void encodingDecodingTest_1() {
        // given
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(NOW)
                .expiresAt(EXPIRATION)
                .subject(TEST_ID.toString())
                .claim(tokenRoleClaim, TEST_ROLE)
                .claim(tokenTypeClaim, EXPECTED_TOKEN_TYPE)
                .build();

        // when
        Jwt encodedJwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        // then
        assertNotNull(encodedJwt, "encoded jwt is null");

        Jwt decodedJwt = jwtDecoder.decode(encodedJwt.getTokenValue());
        assertEquals(TEST_ID.toString(), decodedJwt.getSubject(),
                "wrong or missing subject (user id) claim in encoded jwt");
        assertEquals(TEST_ROLE.toString(), decodedJwt.getClaim(tokenRoleClaim),
                "wrong or missing user role claim in encoded jwt");
        assertEquals(EXPIRATION.truncatedTo(ChronoUnit.SECONDS), decodedJwt.getExpiresAt().truncatedTo(ChronoUnit.SECONDS),
                "wrong or missing expiration claim in encoded jwt");
    }

    @Test
    @DisplayName("encode without type claim - should throw JwtException")
    public void encodingDecodingTest_2() {
        // given
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(NOW)
                .expiresAt(EXPIRATION)
                .subject(TEST_ID.toString())
                .claim(tokenRoleClaim, TEST_ROLE)
                .build();

        // when
        Jwt encodedJwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        // then
        assertThrows(JwtException.class, () -> jwtDecoder.decode(encodedJwt.getTokenValue()));
    }

    @Test
    @DisplayName("encode with invalid type claim - should throw JwtException")
    public void encodingDecodingTest_3() {
        // given
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(NOW)
                .expiresAt(EXPIRATION)
                .subject(TEST_ID.toString())
                .claim(tokenRoleClaim, TEST_ROLE)
                .claim(tokenTypeClaim, "!NON_EXISTING_TYPE!")
                .build();

        // when
        Jwt encodedJwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        // then
        assertThrows(JwtException.class, () -> jwtDecoder.decode(encodedJwt.getTokenValue()));
    }

    @Test
    @DisplayName("encode without role claim - should throw JwtException")
    public void encodingDecodingTest_4() {
        // given
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(NOW)
                .expiresAt(EXPIRATION)
                .subject(TEST_ID.toString())
                .claim(tokenTypeClaim, EXPECTED_TOKEN_TYPE)
                .build();

        // when
        Jwt encodedJwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        // then
        assertThrows(JwtException.class, () -> jwtDecoder.decode(encodedJwt.getTokenValue()));
    }

    @Test
    @DisplayName("encode with invalid role claim - should throw JwtException")
    public void encodingDecodingTest_5() {
        // given
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(NOW)
                .expiresAt(EXPIRATION)
                .subject(TEST_ID.toString())
                .claim(tokenRoleClaim, "!NON_EXISTING_ROLE!")
                .claim(tokenTypeClaim, EXPECTED_TOKEN_TYPE)
                .build();

        // when
        Jwt encodedJwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        // then
        assertThrows(JwtException.class, () -> jwtDecoder.decode(encodedJwt.getTokenValue()));
    }

    @Test
    @DisplayName("encode with invalid subject claim (not UUID) - should throw JwtException")
    public void encodingDecodingTest_6() {
        // given
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(NOW)
                .expiresAt(EXPIRATION)
                .subject("!NOT_UUID!")
                .claim(tokenRoleClaim, TEST_ROLE)
                .claim(tokenTypeClaim, EXPECTED_TOKEN_TYPE)
                .build();

        // when
        Jwt encodedJwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        // then
        assertThrows(JwtException.class, () -> jwtDecoder.decode(encodedJwt.getTokenValue()));
    }
}
