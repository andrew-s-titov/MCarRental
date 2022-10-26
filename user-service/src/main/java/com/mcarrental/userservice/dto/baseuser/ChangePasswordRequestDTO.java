package com.mcarrental.userservice.dto.baseuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChangePasswordRequestDTO {

    @NotBlank(message = "{validation.password.not-blank}")
    private String oldPassword;

    @NotBlank(message = "{validation.password.not-blank}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$",
            message = "{validation.password.pattern}")
    private String newPassword;

    @JsonIgnore
    @AssertTrue(message = "{validation.password.new-equals-old}")
    public boolean newPassword() {
        return !newPassword.equals(oldPassword);
    }
}