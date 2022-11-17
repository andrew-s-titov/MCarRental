package com.mcarrental.jwtauthservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcarrental.jwtauthservice.config.CustomBearerAuthenticationEntryPoint;
import com.mcarrental.jwtauthservice.config.SecurityConfig;
import com.mcarrental.jwtauthservice.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.mcarrental.TestVars.TEST_TOKEN;
import static com.mcarrental.TestVars.USER_INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicController.class, properties = "spring.config.on-not-found = ignore")
@TestPropertySource(locations = "/application-test.yaml")
@Import({SecurityConfig.class, CustomBearerAuthenticationEntryPoint.class, Jackson2ObjectMapperBuilder.class})
public class PublicControllerTest {

    @MockBean
    private TokenService tokenService;
    @MockBean
    private JwtDecoder jwtDecoder;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("refresh token endpoint with valid data - should return status 200, refresh access tokens")
    public void refreshTokenTest_1() throws Exception {
        // given
        when(tokenService.refreshTokens(any())).thenReturn(TEST_TOKEN);

        // when & then
        MvcResult mvcResult = mockMvc.perform(post("/auth/public/tokens/refresh").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(TEST_TOKEN, mvcResult.getResponse().getContentAsString(), "unexpected response body");
        verify(tokenService, only()).refreshTokens(any());
    }

    @Test
    @DisplayName("refresh token endpoint without JWT - should return status 401 without calling TokenService")
    public void refreshTokenTest_2() throws Exception {
        // when & then
        mockMvc.perform(post("/auth/public/tokens/refresh"))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
        verify(tokenService, never()).refreshTokens(any());
    }

    @Test
    @DisplayName("user info endpoint with valid data - should return status 200, respond with user info")
    public void userInfoTest_1() throws Exception {
        // given
        when(tokenService.userInfo(any())).thenReturn(USER_INFO);

        // when & then
        mockMvc.perform(get("/auth/public/user_info").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_INFO)));
        verify(tokenService, only()).userInfo(any());
    }

    @Test
    @DisplayName("user info endpoint without JWT - should return status 401 without calling TokenService")
    public void userInfoTest_2() throws Exception {
        // when & then
        mockMvc.perform(get("/auth/public/user_info"))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
        verify(tokenService, never()).refreshTokens(any());
    }
}
