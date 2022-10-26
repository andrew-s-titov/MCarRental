package com.mcarrental.userservice.service;

import com.mcarrental.userservice.dto.client.ClientCreateRequestDTO;
import com.mcarrental.userservice.dto.client.ClientUpdateRequestDTO;
import com.mcarrental.userservice.dto.client.ClientViewDTO;
import org.springframework.security.access.annotation.Secured;

import java.util.UUID;

import static com.mcarrental.userservice.security.Role.Code.ADMIN_MAIN;
import static com.mcarrental.userservice.security.Role.Code.ADMIN_MANAGER;
import static com.mcarrental.userservice.security.Role.Code.CAR_OWNER;
import static com.mcarrental.userservice.security.Role.Code.CLIENT;

public interface ClientService {

    ClientViewDTO create(ClientCreateRequestDTO createRequest);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER, CLIENT})
    ClientViewDTO get(UUID clientId);

    @Secured(CLIENT)
    ClientViewDTO update(UUID clientId, ClientUpdateRequestDTO updateRequest);
}