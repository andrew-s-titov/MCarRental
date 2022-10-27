package com.mcarrental.userservice.service.impl;

import com.mcarrental.userservice.dto.baseuser.ChangePasswordRequestDTO;
import com.mcarrental.userservice.dto.baseuser.NewPasswordRequestDTO;
import com.mcarrental.userservice.event.EmailVerificationCreatedEvent;
import com.mcarrental.userservice.event.PasswordResetEvent;
import com.mcarrental.userservice.exception.AuthenticationErrorException;
import com.mcarrental.userservice.exception.BadRequestException;
import com.mcarrental.userservice.exception.ConflictException;
import com.mcarrental.userservice.model.BaseUser;
import com.mcarrental.userservice.model.EmailVerification;
import com.mcarrental.userservice.model.PasswordResetToken;
import com.mcarrental.userservice.repository.BaseUserRepository;
import com.mcarrental.userservice.repository.EmailVerificationRepository;
import com.mcarrental.userservice.repository.PasswordResetTokenRepository;
import com.mcarrental.userservice.security.SecurityInfoManager;
import com.mcarrental.userservice.security.SecurityTokensDTO;
import com.mcarrental.userservice.security.UserInfoDTO;
import com.mcarrental.userservice.service.AuthServiceClient;
import com.mcarrental.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AuthServiceImpl implements AuthService {

    public static final String BASIC_AUTH_SCHEME = "Basic";
    public static final String BASIC_DELIMITER = ":";
    private static final Charset basicCredentialsCharset = StandardCharsets.UTF_8;

    private final AuthServiceClient authServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final BaseUserRepository baseUserRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SecurityInfoManager securityInfoManager;

    @Value("${security.email-verification.ttl-hours}")
    private int emailVerificationTtlHours;
    @Value("${security.email-verification.code.length}")
    private int verificationCodeLength;
    @Value("${security.reset-password.code.length}")
    private int resetPasswordCodeLength;
    @Value("${security.reset-password.ttl-minutes}")
    private int resetPasswordTtlMinutes;

    @Override
    public SecurityTokensDTO basicLogin(HttpServletRequest request) {
        String basicStringToken = getDecodedBasicStringToken(request);
        UserInfoDTO user = authenticate(basicStringToken);
        return authServiceClient.getTokens(user);
    }

    @Override
    @Transactional
    public void verifyEmail(String code) {
        EmailVerification emailVerification = emailVerificationRepository.findByCode(code).orElseThrow(
                () -> new AuthenticationErrorException("Wrong verification code"));
        checkCodeTtl(emailVerification.getCreatedDate(), emailVerificationTtlHours, ChronoUnit.HOURS);
        BaseUser user = emailVerification.getBaseUser();
        checkEmailVerificationStatus(user);
        user.setEmailConfirmed(true);
        baseUserRepository.save(user);
        emailVerificationRepository.delete(emailVerification);
    }

    @Override
    @Transactional
    public void resendEmailVerification(UUID userId) {
        BaseUser user = safeGetBaseUser(userId);
        checkEmailVerificationStatus(user);
        EmailVerification verification = emailVerificationRepository.findByBaseUser(user)
                .orElseThrow(() -> new IllegalStateException("Verification for user with id " + userId + " not found"));
        verification.newCode(verificationCodeLength);
        verification = emailVerificationRepository.save(verification);
        eventPublisher.publishEvent(new EmailVerificationCreatedEvent(user.getEmail(), verification.getCode()));
    }

    @Override
    @Transactional
    public void resetPassword(String email) {
        BaseUser user = safeGetBaseUserByEmail(email);

        Optional<PasswordResetToken> byBaseUser = passwordResetTokenRepository.findByBaseUser(user);
        PasswordResetToken resetToken;
        if (byBaseUser.isPresent()) {
            resetToken = byBaseUser.get();
            resetToken.newCode(resetPasswordCodeLength);
        } else {
            resetToken = PasswordResetToken.create(user, resetPasswordCodeLength);
            user.setPassword(passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(15)));
            baseUserRepository.save(user);
        }
        passwordResetTokenRepository.save(resetToken);

        eventPublisher.publishEvent(new PasswordResetEvent(user.getEmail(), resetToken.getCode()));
    }

    @Override
    @Transactional(readOnly = true)
    public void validatePasswordReset(String passwordResetCode) {
        validatePasswordResetCode(passwordResetCode);
    }

    @Override
    @Transactional
    public void newPasswordAfterReset(NewPasswordRequestDTO newPasswordRequest) {
        PasswordResetToken passwordResetToken = validatePasswordResetCode(newPasswordRequest.getCode());
        BaseUser user = passwordResetToken.getBaseUser();
        user.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
        baseUserRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    @Override
    public void changePassword(UUID userId, ChangePasswordRequestDTO changePasswordRequest) {
        BaseUser user = safeGetBaseUser(userId);
        securityInfoManager.checkOwnerOrClientRights(userId);
        boolean oldPasswordMatches = passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword());
        if (!oldPasswordMatches) {
            throw new AuthenticationErrorException("Wrong old password");
        }
        changePasswordRequest.setOldPassword("");
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
    }

    @NonNull
    private String getDecodedBasicStringToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            throw new BadRequestException("Authentication header expected, but not found");
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, BASIC_AUTH_SCHEME)) {
            throw new BadRequestException("Authentication token type must be " + BASIC_AUTH_SCHEME);
        }
        if (header.equalsIgnoreCase(BASIC_AUTH_SCHEME)) {
            throw new BadRequestException("Empty basic authentication token");
        }
        byte[] base64Token = header.substring(6).getBytes(basicCredentialsCharset);
        byte[] decoded = decode(base64Token);
        String token = new String(decoded, basicCredentialsCharset);
        if (!token.contains(BASIC_DELIMITER)) {
            throw new BadRequestException("Invalid basic authentication token: cannot retrieve username and password");
        }
        return token;
    }

    @NonNull
    private byte[] decode(byte[] base64Token) {
        try {
            return Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Authentication token must be base64 encoded");
        }
    }

    @NonNull
    private UserInfoDTO authenticate(String decodedStringBasicToken) {
        String userEmail = decodedStringBasicToken.substring(0, decodedStringBasicToken.indexOf(BASIC_DELIMITER));
        BaseUser user = baseUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AuthenticationErrorException("Wrong login (email)"));
        boolean matches = passwordEncoder.matches(
                decodedStringBasicToken.substring(decodedStringBasicToken.indexOf(BASIC_DELIMITER) + 1),
                user.getPassword());
        if (!matches) {
            throw new AuthenticationErrorException("Wrong password");
        }
        if (!user.isEmailConfirmed()) {
            throw new AuthenticationErrorException("Email not verified", Collections.singletonMap("user_id", user.getId()));
        }
        if (!user.isActive()) {
            throw new AuthenticationErrorException("User is banned", Collections.singletonMap("user_id", user.getId()));
        }
        return new UserInfoDTO(user.getId(), user.getRole());
    }

    private BaseUser safeGetBaseUser(UUID id) {
        return baseUserRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("BaseUser with id " + id + " not found"));
    }

    private BaseUser safeGetBaseUserByEmail(String email) {
        return baseUserRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("BaseUser with email " + email + " not found"));
    }

    private void checkCodeTtl(LocalDateTime createdTimestamp, int ttlHours, TemporalUnit temporalUnit) {
        LocalDateTime now = LocalDateTime.now();
        if (now.minus(ttlHours, temporalUnit).isAfter(createdTimestamp)) {
            throw new AuthenticationErrorException("Code expired");
        }
    }

    private void checkEmailVerificationStatus(BaseUser user) {
        if (user.isEmailConfirmed()) {
            throw new ConflictException("Email " + user.getEmail() + " already confirmed");
        }
    }

    private PasswordResetToken validatePasswordResetCode(String passwordResetCode) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByCode(passwordResetCode).orElseThrow(
                () -> new AuthenticationErrorException("Wrong password reset token"));
        checkCodeTtl(passwordResetToken.getCreatedDate(), resetPasswordTtlMinutes, ChronoUnit.MINUTES);
        return passwordResetToken;
    }
}