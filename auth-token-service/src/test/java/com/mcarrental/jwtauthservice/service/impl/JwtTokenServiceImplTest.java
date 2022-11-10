package com.mcarrental.jwtauthservice.service.impl;

import com.mcarrental.jwtauthservice.dto.UserInfoDTO;
import com.mcarrental.jwtauthservice.exception.InvalidRequestException;
import com.mcarrental.jwtauthservice.security.TokenType;
import com.mcarrental.jwtauthservice.service.TokenCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.mcarrental.TestVars.TEST_ID;
import static com.mcarrental.TestVars.TEST_ROLE;
import static com.mcarrental.TestVars.TEST_TOKEN;
import static com.mcarrental.TestVars.USER_INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.yaml")
@Import({JwtTokenServiceImpl.class})
public class JwtTokenServiceImplTest {

    @Autowired
    private JwtTokenServiceImpl jwtTokenService;
    @MockBean
    private TokenCreator tokenCreator;

    @Value("${security.jwt.claim.type}")
    private String tokenTypeClaim;
    @Value("${security.jwt.claim.role}")
    private String tokenRoleClaim;

    @Test
    @DisplayName("createTokens() with a valid argument - should return valid tokens")
    public void createTokensTest_1() {
        // given
        when(tokenCreator.createAccessToken(any())).thenReturn(TEST_TOKEN);
        when(tokenCreator.createRefreshToken(any())).thenReturn(TEST_TOKEN);

        // when
        var tokens = jwtTokenService.createTokens(USER_INFO);

        // then
        assertNotNull(tokens, "method returned null tokens");
        assertTrue(StringUtils.isNotBlank(tokens.getAccessToken()), "returned blank access token");
        assertTrue(StringUtils.isNotBlank(tokens.getRefreshToken()), "returned blank refresh token");

        verify(tokenCreator, times(1)).createAccessToken(USER_INFO);
        verify(tokenCreator, times(1)).createRefreshToken(USER_INFO);
    }

    @Test
    @DisplayName("createTokens() with a invalid argument - should throw IAE")
    public void createTokensTest_2() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> jwtTokenService.createTokens(null));
        verifyNoInteractions(tokenCreator);
    }

    @Test
    @DisplayName("userInfo() with a valid argument - should return not null correct info")
    public void userInfoTest_1() {
        // given
        JwtAuthenticationToken jwtAuthTokenMock = Mockito.mock(JwtAuthenticationToken.class);
        Jwt mockJwt = Mockito.mock(Jwt.class);
        when(jwtAuthTokenMock.getToken()).thenReturn(mockJwt);
        when(mockJwt.getSubject()).thenReturn(TEST_ID.toString());
        when(mockJwt.getClaim(tokenRoleClaim)).thenReturn(TEST_ROLE.toString());
        when(mockJwt.getClaim(tokenTypeClaim)).thenReturn(TokenType.ACCESS.toString());

        // when
        UserInfoDTO userInfo = jwtTokenService.userInfo(jwtAuthTokenMock);

        // then
        assertNotNull(userInfo, "method returned null userInfo");
        assertEquals(TEST_ID, userInfo.getUserId(), "incorrect subject (user id) from jwt");
        assertEquals(TEST_ROLE, userInfo.getUserRole(), "incorrect role from jwt");
    }

    @Test
    @DisplayName("userInfo() with an invalid argument - should throw IAE")
    public void userInfoTest_2() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> jwtTokenService.userInfo(null));
        verifyNoInteractions(tokenCreator);
    }

    @Test
    @DisplayName("refreshTokens() with a valid argument - should return new valid refresh token")
    public void refreshAccessTokensTest_1() {
        // given
        JwtAuthenticationToken jwtAuthTokenMock = Mockito.mock(JwtAuthenticationToken.class);
        Jwt mockJwt = Mockito.mock(Jwt.class);
        when(jwtAuthTokenMock.getToken()).thenReturn(mockJwt);
        when(mockJwt.getSubject()).thenReturn(TEST_ID.toString());
        when(mockJwt.getClaim(tokenRoleClaim)).thenReturn(TEST_ROLE.toString());
        when(mockJwt.getClaim(tokenTypeClaim)).thenReturn(TokenType.REFRESH.toString());
        when(tokenCreator.createAccessToken(any())).thenReturn(TEST_TOKEN);

        // when
        String newAccessToken = jwtTokenService.refreshTokens(jwtAuthTokenMock);

        // then
        assertEquals(TEST_TOKEN, newAccessToken, "unexpected token returned");
        verify(tokenCreator, only()).createAccessToken(isA(UserInfoDTO.class));
    }

    @Test
    @DisplayName("refreshTokens() with an invalid (null) argument - should throw IAE")
    public void refreshAccessTokensTest_2() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> jwtTokenService.refreshTokens(null));
        verifyNoInteractions(tokenCreator);
    }

    @Test
    @DisplayName("refreshTokens() with wrong token type - should throw InvalidRequestException")
    public void refreshAccessTokensTest_3() {
        // given
        JwtAuthenticationToken jwtAuthTokenMock = Mockito.mock(JwtAuthenticationToken.class);
        Jwt mockJwt = Mockito.mock(Jwt.class);
        when(jwtAuthTokenMock.getToken()).thenReturn(mockJwt);
        // Refresh token is expected here
        when(mockJwt.getClaim(tokenTypeClaim)).thenReturn(TokenType.ACCESS.toString());
        when(mockJwt.getSubject()).thenReturn(TEST_ID.toString());
        when(mockJwt.getClaim(tokenRoleClaim)).thenReturn(TEST_ROLE.toString());
        when(tokenCreator.createAccessToken(any())).thenReturn(TEST_TOKEN);

        // when & then
        assertThrows(InvalidRequestException.class, () -> jwtTokenService.refreshTokens(jwtAuthTokenMock));
        verifyNoInteractions(tokenCreator);
    }
}