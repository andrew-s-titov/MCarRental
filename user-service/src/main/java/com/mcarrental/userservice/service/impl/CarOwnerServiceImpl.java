package com.mcarrental.userservice.service.impl;

import com.mcarrental.userservice.converter.CarOwnerConverter;
import com.mcarrental.userservice.dto.ItemsDTO;
import com.mcarrental.userservice.dto.carowner.CarOwnerCreateRequestDTO;
import com.mcarrental.userservice.dto.carowner.CarOwnerUpdateRequestDTO;
import com.mcarrental.userservice.dto.carowner.CarOwnerViewDTO;
import com.mcarrental.userservice.enums.CompanyType;
import com.mcarrental.userservice.model.CarOwner;
import com.mcarrental.userservice.repository.BaseUserRepository;
import com.mcarrental.userservice.repository.CarOwnerRepository;
import com.mcarrental.userservice.security.SecurityInfoManager;
import com.mcarrental.userservice.service.CarOwnerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CarOwnerServiceImpl implements CarOwnerService {

    private final BaseUserRepository baseUserRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final CarOwnerConverter converter;
    private final PasswordEncoder passwordEncoder;
    private final SecurityInfoManager securityInfoManager;

    @Transactional
    @Override
    public CarOwnerViewDTO create(CarOwnerCreateRequestDTO createRequest) {
        checkEmailUniqueness(createRequest.getEmail());
        checkRegNumberUniqueness(createRequest.getRegNumber());
        CarOwner carOwner = converter.fromCreateRequest(createRequest);
        carOwner.setPassword(passwordEncoder.encode(createRequest.getPassword()));
        createRequest.setPassword("");
        return converter.toView(carOwnerRepository.save(carOwner));
    }

    @Override
    public ItemsDTO<CarOwnerViewDTO> getAll(Pageable pageable) {
        return ItemsDTO.fromPage(carOwnerRepository.findAll(pageable)
                .map(converter::toView));
    }

    @Override
    public CarOwnerViewDTO getByRegNumber(String regNumber) {
        return converter.toView(safeGetCarOwner(regNumber));
    }

    @Override
    public CarOwnerViewDTO getById(UUID carOwnerId) {
        return converter.toView(safeGetCarOwner(carOwnerId));
    }

    @Override
    public CarOwnerViewDTO updateCarOwner(UUID carOwnerId, CarOwnerUpdateRequestDTO updateRequest) {
        CarOwner carOwner = safeGetCarOwner(carOwnerId);
        securityInfoManager.checkSameUser(carOwnerId);
        boolean hasNewData = mergeCarOwnerWithUpdate(carOwner, updateRequest);
        if (hasNewData) {
            carOwnerRepository.save(carOwner);
        }
        return converter.toView(carOwner);
    }

    private CarOwner safeGetCarOwner(UUID carOwnerId) {
        return carOwnerRepository.findById(carOwnerId)
                .orElseThrow(() -> new EntityNotFoundException("Car owner with id " + carOwnerId + " not found"));
    }

    private CarOwner safeGetCarOwner(String regNumber) {
        return carOwnerRepository.findByRegNumber(regNumber)
                .orElseThrow(() -> new EntityNotFoundException("Car owner with registration number " + regNumber + " not found"));
    }

    private void checkRegNumberUniqueness(String regNumber) {
        if (carOwnerRepository.existsByRegNumber(regNumber)) {
            throw new EntityExistsException(
                    "Car owner with registration number " + regNumber + " is already registered");
        }
    }

    private boolean mergeCarOwnerWithUpdate(CarOwner carOwner, CarOwnerUpdateRequestDTO updateRequest) {
        boolean hasNewData = false;
        String newName = updateRequest.getName();
        if (StringUtils.isNotBlank(newName) && !newName.equals(carOwner.getName())) {
            carOwner.setName(newName);
            hasNewData = true;
        }
        CompanyType newType = updateRequest.getCompanyType();
        if (newType != null && newType.equals(carOwner.getCompanyType())) {
            carOwner.setCompanyType(newType);
            hasNewData = true;
        }
        String newLegalAddress = updateRequest.getLegalAddress();
        if (StringUtils.isNotBlank(newLegalAddress) && !newLegalAddress.equals(carOwner.getLegalAddress())) {
            carOwner.setLegalAddress(newLegalAddress);
            hasNewData = true;
        }
        String newPickupAddress = updateRequest.getPickupAddress();
        if (StringUtils.isNotBlank(newPickupAddress) && !newPickupAddress.equals(carOwner.getPickupAddress())) {
            carOwner.setPickupAddress(newPickupAddress);
            hasNewData = true;
        }
        String newPhoneNumber = updateRequest.getPhone();
        if (StringUtils.isNotBlank(newPhoneNumber) && !newPhoneNumber.equals(carOwner.getPhone())) {
            carOwner.setPhone(newPhoneNumber);
            hasNewData = true;
        }
        return hasNewData;
    }

    private void checkEmailUniqueness(@NonNull String email) {
        if (baseUserRepository.existsByEmail(email)) {
            throw new EntityExistsException("Client with email '" + email + "' is already registered");
        }
    }
}