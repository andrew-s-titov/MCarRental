package com.mcarrental.jwtauthservice.controller;

import com.mcarrental.jwtauthservice.dto.SecurityTokensDTO;
import com.mcarrental.jwtauthservice.dto.UserInfoDTO;
import com.mcarrental.jwtauthservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class PrivateController {

    private final TokenService tokenService;

    @PostMapping("/auth/private/tokens")
    public ResponseEntity<SecurityTokensDTO> createTokens(@Valid @RequestBody UserInfoDTO userInfo) {
        return ResponseEntity.ok(tokenService.createTokens(userInfo));
    }
}