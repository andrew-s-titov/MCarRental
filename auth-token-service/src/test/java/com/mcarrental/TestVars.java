package com.mcarrental;

import com.mcarrental.jwtauthservice.dto.UserInfoDTO;
import com.mcarrental.jwtauthservice.security.Role;

import java.util.UUID;

public abstract class TestVars {
    public final static UUID TEST_ID = UUID.randomUUID();
    public final static Role TEST_ROLE = Role.CLIENT;
    public final static UserInfoDTO USER_INFO = new UserInfoDTO(TEST_ID, TEST_ROLE);
    public final static String TEST_TOKEN = "test_token";
}