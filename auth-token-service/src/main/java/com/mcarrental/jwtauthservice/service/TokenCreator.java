package com.mcarrental.jwtauthservice.service;

import com.mcarrental.jwtauthservice.dto.UserInfoDTO;

public interface TokenCreator {

    String createAccessToken(UserInfoDTO userInfo);

    String createRefreshToken(UserInfoDTO userInfo);
}
