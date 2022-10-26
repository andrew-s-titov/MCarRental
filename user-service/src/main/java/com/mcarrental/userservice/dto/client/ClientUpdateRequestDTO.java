package com.mcarrental.userservice.dto.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientUpdateRequestDTO {

    @Size(min = 3, max = 30, message = "{validation.first-name.size}")
    private String firstName;

    @Size(min = 3, max = 30, message = "{validation.last-name.size}")
    private String lastName;

    @Pattern(regexp = "^\\+375[0-9]{9}$", message = "{validation.phone.pattern}")
    private String phone;
}