package com.mcarrental.userservice.controller;

import com.mcarrental.userservice.dto.baseuser.ChangePasswordRequestDTO;
import com.mcarrental.userservice.dto.baseuser.NewPasswordRequestDTO;
import com.mcarrental.userservice.security.SecurityTokensDTO;
import com.mcarrental.userservice.service.AuthService;
import com.mcarrental.userservice.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/simple")
    public ResponseEntity<SecurityTokensDTO> simpleLogin(HttpServletRequest request) {
        return ResponseEntity.ok(authService.basicLogin(request));
    }

    @RequestMapping(value = "/verify", method = {RequestMethod.GET, RequestMethod.PUT})
    // TODO: restrict to PUT mapping when FE redirect page is provided for verification
    public void verifyEmail(@RequestParam("code") String code) {
        authService.verifyEmail(code);
    }

    @PostMapping(value = "/verify/resend/{clientId}")
    public void resendEmailVerification(@PathVariable("clientId") UUID clientId) {
        authService.resendEmailVerification(clientId);
    }

    @PutMapping("/password_reset/{email}")
    public void forgetPassword(@PathVariable String email) {
        authService.resetPassword(email);
    }

    @GetMapping("/password_reset")
    public void validatePasswordResetToken(@RequestParam("code") String code) {
        authService.validatePasswordReset(code);
    }

    @PostMapping("/password_reset")
    public void newPasswordAfterReset(@Valid @RequestBody NewPasswordRequestDTO newPasswordRequest) {
        authService.newPasswordAfterReset(newPasswordRequest);
    }

    @GetMapping("/oauth2/google/url_for_auth_code")
    public ResponseEntity<?> urlForGoogleAuthCode() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/oauth2/google/login/{authCode}")
    public ResponseEntity<?> googleLogin(@PathVariable("authCode") String authCode) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/password" + RestUtil.UUID_V4_PATH)
    public void changePassword(@PathVariable("id") UUID userId, @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {
        authService.changePassword(userId, changePasswordRequest);
    }
}