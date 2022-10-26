package com.mcarrental.carsearchservice.service;

import com.mcarrental.carsearchservice.dto.CarSearchRequestDTO;
import com.mcarrental.carsearchservice.dto.CarViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarSearchService {

    Page<CarViewDTO> searchCars(CarSearchRequestDTO carSearchRequest, Pageable pageable);
}