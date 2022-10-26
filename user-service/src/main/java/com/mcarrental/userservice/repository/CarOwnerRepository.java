package com.mcarrental.userservice.repository;

import com.mcarrental.userservice.model.CarOwner;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CarOwnerRepository extends UserRepository<CarOwner>{

    boolean existsByRegNumber(@NonNull String regNumber);

    Optional<CarOwner> findByRegNumber(@NonNull String regNumber);
}
