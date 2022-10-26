package com.mcarrental.userservice.service;

import com.mcarrental.userservice.security.SecurityTokensDTO;
import com.mcarrental.userservice.security.UserInfoDTO;

public interface AuthServerCaller {

    SecurityTokensDTO getTokens(UserInfoDTO user);
}