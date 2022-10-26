package com.mcarrental.carsearchservice.controller;

import com.mcarrental.carsearchservice.dto.CarSearchRequestDTO;
import com.mcarrental.carsearchservice.dto.CarViewDTO;
import com.mcarrental.carsearchservice.dto.ItemsDTO;
import com.mcarrental.carsearchservice.service.CarSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/car/search")
public class CarSearchController {

    private final CarSearchService carSearchService;

    @GetMapping
    public ResponseEntity<ItemsDTO<CarViewDTO>> searchCars(@Valid @RequestBody CarSearchRequestDTO carSearchRequest,
                                                           Pageable pageable) {
        var carsPage = carSearchService.searchCars(carSearchRequest, pageable);
        return ResponseEntity.ok(ItemsDTO.fromPage(carsPage));
    }
}