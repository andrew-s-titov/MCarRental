package com.mcarrental.userservice.repository;

import com.mcarrental.userservice.model.PasswordResetToken;
import com.mcarrental.userservice.model.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByBaseUser(BaseUser baseUser);

    Optional<PasswordResetToken> findByCode(String code);
}