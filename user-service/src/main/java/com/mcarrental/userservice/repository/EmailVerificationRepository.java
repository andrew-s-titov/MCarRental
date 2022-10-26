package com.mcarrental.userservice.repository;

import com.mcarrental.userservice.model.BaseUser;
import com.mcarrental.userservice.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {

    Optional<EmailVerification> findByBaseUser(BaseUser user);

    Optional<EmailVerification> findByCode(String code);
}