package com.mcarrental.carservice.service;

import com.mcarrental.carservice.dto.ItemsDTO;
import com.mcarrental.carservice.dto.car.CarCreateRequestDTO;
import com.mcarrental.carservice.dto.car.CarUpdateRequestDTO;
import com.mcarrental.carservice.dto.car.CarViewDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;

import java.util.UUID;

import static com.mcarrental.carservice.security.Role.Code.ADMIN_MAIN;
import static com.mcarrental.carservice.security.Role.Code.ADMIN_MANAGER;
import static com.mcarrental.carservice.security.Role.Code.CAR_OWNER;

public interface CarService {

    @Secured(CAR_OWNER)
    @NonNull
    CarViewDTO create(@NonNull CarCreateRequestDTO createRequest);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER})
    @NonNull
    ItemsDTO<CarViewDTO> getAll(UUID ownerId, Pageable pageable);

    @NonNull
    CarViewDTO getById(@NonNull UUID carId);

    @NonNull
    String getShortInfo(@NonNull UUID carId);

    @Secured(CAR_OWNER)
    @NonNull
    CarViewDTO update(@NonNull UUID carId, CarUpdateRequestDTO updateRequest);

    @Secured(CAR_OWNER)
    void changePrice(@NonNull UUID carId, Integer newPricePerDay);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER})
    void activation(@NonNull UUID carId, boolean visible);

    // TODO: do we need to allow removing? history tracking?
    @Secured(CAR_OWNER)
    void remove(@NonNull UUID carId);
}