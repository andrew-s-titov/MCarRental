package com.mcarrental.userservice.dto.carowner;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.userservice.enums.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarOwnerUpdateRequestDTO {

    private CompanyType companyType;

    @Size(min = 3, max = 100, message = "{validation.owner.name.size}")
    private String name;

    @Size(min = 3, max = 200, message = "{validation.legal-address.size}")
    private String legalAddress;

    @Size(min = 3, max = 200, message = "{validation.pickup-address.size}")
    private String pickupAddress;

    @Pattern(regexp = "^\\+375[0-9]{9}$", message = "{validation.phone.pattern}")
    private String phone;
}