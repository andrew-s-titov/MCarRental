package com.mcarrental.userservice.dto.baseuser;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class BaseUserCreateRequestDTO {

    @NotBlank(message = "{validation.email.not-blank}")
    @Email(message = "{validation.email}")
    private String email;

    @NotBlank(message = "{validation.password.not-blank}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$",
            message = "{validation.password.pattern}")
    private String password;

    @NotBlank(message = "{validation.phone.not-blank}")
    @Pattern(regexp = "^\\+375[0-9]{9}$", message = "{validation.phone.pattern}")
    private String phone;
}
