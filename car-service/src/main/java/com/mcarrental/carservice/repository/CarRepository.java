package com.mcarrental.carservice.repository;

import com.mcarrental.carservice.dto.car.CarViewDTO;
import com.mcarrental.carservice.model.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {

    Slice<CarViewDTO> findAllByOwnerId(UUID ownerId, Pageable pageable);

    Slice<CarViewDTO> findAllBy(Pageable pageable);

    Optional<Car> findByVin(String vinNumber);

    boolean existsByVin(String vinNumber);
}