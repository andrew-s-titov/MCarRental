package com.mcarrental.jwtauthservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcarrental.jwtauthservice.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static com.mcarrental.TestVars.TEST_TOKEN;
import static com.mcarrental.TestVars.USER_INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.config.on-not-found = ignore")
@TestPropertySource(locations = "/application-test.yaml")
@AutoConfigureMockMvc
public class PublicControllerTest {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private JwtDecoder jwtDecoder;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${security.jwt.claim.role}")
    private String tokenRoleClaim;

    @Test
    @DisplayName("refresh token endpoint with valid data - should return status 200, refresh access tokens")
    public void refreshTokenTest_1() throws Exception {
        // given
        when(tokenService.refreshTokens(any())).thenReturn(TEST_TOKEN);
        Jwt mockJwt = Mockito.mock(Jwt.class);
        when(jwtDecoder.decode(TEST_TOKEN)).thenReturn(mockJwt);

        // when & then
        MvcResult mvcResult = mockMvc.perform(post("/auth/public/tokens/refresh")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(TEST_TOKEN, mvcResult.getResponse().getContentAsString(), "unexpected response body");
        verify(tokenService, only()).refreshTokens(any());
    }

    @Test
    @DisplayName("user info endpoint with valid data - should return status 200, respond with user info")
    public void userInfoTest_1() throws Exception {
        // given
        when(tokenService.userInfo(any())).thenReturn(USER_INFO);
        Jwt mockJwt = Mockito.mock(Jwt.class);
        when(jwtDecoder.decode(TEST_TOKEN)).thenReturn(mockJwt);

        // when & then
        mockMvc.perform(get("/auth/public/user_info")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_INFO)));
        verify(tokenService, only()).userInfo(any());
    }
}
