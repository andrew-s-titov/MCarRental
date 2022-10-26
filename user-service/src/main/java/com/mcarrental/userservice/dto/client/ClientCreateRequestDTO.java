package com.mcarrental.userservice.dto.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.userservice.dto.baseuser.BaseUserCreateRequestDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientCreateRequestDTO extends BaseUserCreateRequestDTO {

    @NotBlank(message = "{validation.first-name.not-blank}")
    @Size(min = 3, max = 30, message = "{validation.first-name.size}")
    private String firstName;

    @NotBlank(message = "{validation.last-name.not-blank}")
    @Size(min = 3, max = 30, message = "{validation.last-name.size}")
    private String lastName;
}