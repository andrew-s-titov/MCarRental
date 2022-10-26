package com.mcarrental.userservice.dto.carowner;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.userservice.dto.baseuser.BaseUserViewDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarOwnerViewDTO extends BaseUserViewDTO {

    private UUID id;

    private String regNumber;

    private String fullName;

    private String legalAddress;

    private String pickupAddress;
}