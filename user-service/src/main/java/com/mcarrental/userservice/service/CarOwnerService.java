package com.mcarrental.userservice.service;

import com.mcarrental.userservice.dto.ItemsDTO;
import com.mcarrental.userservice.dto.carowner.CarOwnerCreateRequestDTO;
import com.mcarrental.userservice.dto.carowner.CarOwnerUpdateRequestDTO;
import com.mcarrental.userservice.dto.carowner.CarOwnerViewDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import java.util.UUID;

import static com.mcarrental.userservice.security.Role.Code.ADMIN_MAIN;
import static com.mcarrental.userservice.security.Role.Code.ADMIN_MANAGER;
import static com.mcarrental.userservice.security.Role.Code.CAR_OWNER;
import static com.mcarrental.userservice.security.Role.Code.CLIENT;

public interface CarOwnerService {

    CarOwnerViewDTO create(CarOwnerCreateRequestDTO createRequest);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER})
    ItemsDTO<CarOwnerViewDTO> getAll(Pageable pageable);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER})
    CarOwnerViewDTO getByRegNumber(String regNumber);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER, CLIENT})
    CarOwnerViewDTO getById(UUID id);

    @Secured(CAR_OWNER)
    CarOwnerViewDTO updateCarOwner(UUID id, CarOwnerUpdateRequestDTO updateRequest);
}
