package com.mcarrental.userservice.controller;

import com.mcarrental.userservice.dto.ItemsDTO;
import com.mcarrental.userservice.dto.carowner.CarOwnerCreateRequestDTO;
import com.mcarrental.userservice.dto.carowner.CarOwnerUpdateRequestDTO;
import com.mcarrental.userservice.dto.carowner.CarOwnerViewDTO;
import com.mcarrental.userservice.service.CarOwnerService;
import com.mcarrental.userservice.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/car_owner")
public class CarOwnerController {

    private final CarOwnerService carOwnerService;

    @PostMapping
    public ResponseEntity<CarOwnerViewDTO> create(@Valid @RequestBody CarOwnerCreateRequestDTO createRequest) {
        CarOwnerViewDTO newCarOwner = carOwnerService.create(createRequest);
        return ResponseEntity.created(URI.create("/user/car_owner/" + newCarOwner.getId())).body(newCarOwner);
    }

    @GetMapping
    public ResponseEntity<ItemsDTO<CarOwnerViewDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(carOwnerService.getAll(pageable));
    }

    @GetMapping("/reg_number/{regNumber:^\\d{9}$}")
    public ResponseEntity<CarOwnerViewDTO> getByRegNumber(@PathVariable("regNumber") String regNumber) {
        return ResponseEntity.ok(carOwnerService.getByRegNumber(regNumber));
    }

    @GetMapping(RestUtil.UUID_V4_PATH)
    public ResponseEntity<CarOwnerViewDTO> getById(@PathVariable("id") UUID carOwnerId) {
        return ResponseEntity.ok(carOwnerService.getById(carOwnerId));
    }

    @PutMapping(RestUtil.UUID_V4_PATH)
    public ResponseEntity<CarOwnerViewDTO> update(@PathVariable("id") UUID carOwnerId,
                                                  @Valid @RequestBody CarOwnerUpdateRequestDTO updateRequest) {
        return ResponseEntity.ok(carOwnerService.updateCarOwner(carOwnerId, updateRequest));
    }
}