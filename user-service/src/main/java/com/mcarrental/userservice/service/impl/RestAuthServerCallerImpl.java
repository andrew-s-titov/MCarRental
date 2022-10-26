package com.mcarrental.userservice.service.impl;

import com.mcarrental.userservice.security.SecurityTokensDTO;
import com.mcarrental.userservice.security.UserInfoDTO;
import com.mcarrental.userservice.service.AuthServerCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class RestAuthServerCallerImpl implements AuthServerCaller {

    private final RestTemplate restTemplate;
    private final HttpMethod DEFAULT_AUTH_SERVER_METHOD = HttpMethod.POST;

    @Value("${security.auth-server.url}")
    private String authServerUrl;
    @Value("${security.auth-server.method}")
    private String authServerMethod;

    @Override
    public SecurityTokensDTO getTokens(UserInfoDTO user) {
        return restTemplate.exchange(
                authServerUrl,
                authServerMethod(),
                new HttpEntity<>(user),
                SecurityTokensDTO.class).getBody();
    }

    private HttpMethod authServerMethod() {
        HttpMethod method = HttpMethod.resolve(authServerMethod.toUpperCase());
        return method == null ? DEFAULT_AUTH_SERVER_METHOD : method;
    }
}
