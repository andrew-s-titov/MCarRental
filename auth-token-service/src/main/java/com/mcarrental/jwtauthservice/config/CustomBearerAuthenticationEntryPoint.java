package com.mcarrental.jwtauthservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcarrental.jwtauthservice.dto.ApiErrorDTO;
import com.mcarrental.jwtauthservice.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomBearerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = "Invalid JWT";
        if (authException instanceof OAuth2AuthenticationException) {
            OAuth2Error error = ((OAuth2AuthenticationException) authException).getError();
            message = error.getDescription();
        }
        if (authException instanceof InsufficientAuthenticationException) {
            message = "Bearer token is missing";
        }
        ApiErrorDTO apiError = RestUtil.apiError(request, HttpStatus.UNAUTHORIZED, message);
        RestUtil.writeErrorToResponse(response, apiError, objectMapper);
    }
}
