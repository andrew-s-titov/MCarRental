package com.mcarrental.userservice.dto.carowner;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.userservice.dto.baseuser.BaseUserCreateRequestDTO;
import com.mcarrental.userservice.enums.CompanyType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarOwnerCreateRequestDTO extends BaseUserCreateRequestDTO {

    @NotBlank(message = "{validation.owner.reg-number.not-blank}")
    @Pattern(regexp = "^\\d{9}$", message = "{validation.owner.reg-number.pattern}")
    private String regNumber;

    @NotNull(message = "{validation.owner.type.not-null}")
    private CompanyType companyType;

    @NotBlank(message = "{validation.owner.name.not-blank}")
    @Size(min = 3, max = 100, message = "{validation.owner.name.size}")
    private String name;

    @NotBlank(message = "{validation.legal-address.not-blank}")
    @Size(min = 3, max = 200, message = "{validation.legal-address.size}")
    private String legalAddress;

    @NotBlank(message = "{validation.pickup-address.not-blank}")
    @Size(min = 3, max = 200, message = "{validation.pickup-address.size}")
    private String pickupAddress;
}
