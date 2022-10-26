package com.mcarrental.bookingservice.security;

import org.springframework.lang.NonNull;

import java.util.UUID;

public interface SecurityInfoManager {

    @NonNull
    UUID getUserId();

    @NonNull
    Role getUserRole();

    void checkOwnerOrClientRights(UUID expectedUserId);

    void checkSameUser(UUID expectedUserId);
}