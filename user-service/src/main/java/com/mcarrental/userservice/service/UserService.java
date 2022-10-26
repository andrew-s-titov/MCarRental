package com.mcarrental.userservice.service;

import java.util.UUID;

public interface UserService {

    String getEmailById(UUID userId);
}