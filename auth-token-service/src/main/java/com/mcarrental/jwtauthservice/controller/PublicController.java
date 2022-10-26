package com.mcarrental.jwtauthservice.controller;

import com.mcarrental.jwtauthservice.dto.UserInfoDTO;
import com.mcarrental.jwtauthservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PublicController {

    private final TokenService tokenService;

    @PostMapping("/auth/public/tokens/refresh")
    public ResponseEntity<String> refreshTokens(JwtAuthenticationToken jwtAuthToken) {
        return ResponseEntity.ok(tokenService.refreshTokens(jwtAuthToken));
    }

    @GetMapping("/auth/public/user_info")
    public ResponseEntity<UserInfoDTO> getUserInfo(JwtAuthenticationToken jwtAuthToken) {
        return ResponseEntity.ok(tokenService.userInfo(jwtAuthToken));
    }
}