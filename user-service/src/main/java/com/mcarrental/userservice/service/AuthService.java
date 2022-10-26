package com.mcarrental.userservice.service;

import com.mcarrental.userservice.dto.baseuser.ChangePasswordRequestDTO;
import com.mcarrental.userservice.dto.baseuser.NewPasswordRequestDTO;
import com.mcarrental.userservice.security.SecurityTokensDTO;
import org.springframework.security.access.annotation.Secured;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static com.mcarrental.userservice.security.Role.Code.CAR_OWNER;
import static com.mcarrental.userservice.security.Role.Code.CLIENT;

public interface AuthService {

    SecurityTokensDTO basicLogin(HttpServletRequest request);

    void verifyEmail(String code);

    void resendEmailVerification(UUID clientId);

    void resetPassword(String email);

    void validatePasswordReset(String passwordResetCode);

    void newPasswordAfterReset(NewPasswordRequestDTO newPasswordRequest);

    @Secured({CLIENT, CAR_OWNER})
    void changePassword(UUID userId, ChangePasswordRequestDTO changePasswordRequest);
}
