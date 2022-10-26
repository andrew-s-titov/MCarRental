package com.mcarrental.carservice.controller;

import com.mcarrental.carservice.dto.ItemsDTO;
import com.mcarrental.carservice.dto.car.CarCreateRequestDTO;
import com.mcarrental.carservice.dto.car.CarViewDTO;
import com.mcarrental.carservice.dto.car.CarUpdateRequestDTO;
import com.mcarrental.carservice.service.CarService;
import com.mcarrental.carservice.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarViewDTO> newCar(@Valid @RequestBody CarCreateRequestDTO createRequest) {
        CarViewDTO newCar = carService.create(createRequest);
        return ResponseEntity.created(URI.create("/car/" + newCar.getId())).body(newCar);
    }

    @GetMapping
    public ResponseEntity<ItemsDTO<CarViewDTO>> showCars(@RequestParam(value = "ownerId", required = false) UUID ownerId,
                                                         Pageable pageable) {
        return ResponseEntity.ok(carService.getAll(ownerId, pageable));
    }

    @GetMapping(RestUtil.UUID_V4_PATH)
    public ResponseEntity<String> showCar(@PathVariable("id") UUID carId) {
        return ResponseEntity.ok(carService.getShortInfo(carId));
    }

    @GetMapping(RestUtil.UUID_V4_PATH + "/short")
    public ResponseEntity<CarViewDTO> carShortInfo(@PathVariable("id") UUID carId) {
        return ResponseEntity.ok(carService.getById(carId));
    }

    @PutMapping(RestUtil.UUID_V4_PATH)
    public ResponseEntity<CarViewDTO> updateCarInfo(@PathVariable("id") UUID carId, @RequestBody CarUpdateRequestDTO updateRequest) {
        return ResponseEntity.ok(carService.update(carId, updateRequest));
    }

    @PutMapping(RestUtil.UUID_V4_PATH + "/price")
    public void changePrice(@PathVariable("id") UUID carId, @RequestParam("newPrice") Integer newPrice) {
        carService.changePrice(carId, newPrice);
    }

    @PutMapping(RestUtil.UUID_V4_PATH + "/visibility")
    public void activation(@PathVariable("id") UUID carId, @RequestParam("visible") boolean visible) {
        carService.activation(carId, visible);
    }

    @DeleteMapping(RestUtil.UUID_V4_PATH)
    public void remove(@PathVariable("id") UUID carId) {
        carService.remove(carId);
    }
}