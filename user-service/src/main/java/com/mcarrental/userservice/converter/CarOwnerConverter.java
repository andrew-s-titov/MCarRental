package com.mcarrental.userservice.converter;

import com.mcarrental.userservice.dto.carowner.CarOwnerCreateRequestDTO;
import com.mcarrental.userservice.dto.carowner.CarOwnerViewDTO;
import com.mcarrental.userservice.enums.CompanyType;
import com.mcarrental.userservice.model.CarOwner;
import org.springframework.stereotype.Component;

@Component
public class CarOwnerConverter {

    public CarOwner fromCreateRequest(CarOwnerCreateRequestDTO createRequest) {
        return CarOwner.builder()
                .regNumber(createRequest.getRegNumber())
                .companyType(createRequest.getCompanyType())
                .name(createRequest.getName())
                .legalAddress(createRequest.getLegalAddress())
                .pickupAddress(createRequest.getPickupAddress())
                .phone(createRequest.getPhone())
                .email(createRequest.getEmail())
                .active(true)
                .emailConfirmed(false)
                .build();
    }

    public CarOwnerViewDTO toView(CarOwner carOwner) {
        return CarOwnerViewDTO.builder()
                .id(carOwner.getId())
                .regNumber(carOwner.getRegNumber())
                .fullName(ownerFullName(carOwner))
                .legalAddress(carOwner.getLegalAddress())
                .pickupAddress(carOwner.getPickupAddress())
                .phone(carOwner.getPhone())
                .email(carOwner.getEmail())
                .build();
    }

    private String ownerFullName(CarOwner owner) {
        CompanyType type = owner.getCompanyType();
        String name = owner.getName();
        if (!type.equals(CompanyType.IE)) {
            name = "'" + name + "'";
        }
        return type.getLocalizedName() + " " + name;
    }
}
