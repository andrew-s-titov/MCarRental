package com.mcarrental.userservice.service;

import com.mcarrental.userservice.security.SecurityTokensDTO;
import com.mcarrental.userservice.security.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "${security.auth-server.name}")
public interface AuthServiceClient {

    @RequestMapping(method = RequestMethod.POST, path = "${security.auth-server.url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    SecurityTokensDTO getTokens(UserInfoDTO user);
}