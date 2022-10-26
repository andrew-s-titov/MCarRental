package com.mcarrental.userservice.repository;

import com.mcarrental.userservice.model.BaseUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseUserRepository extends UserRepository<BaseUser> {

    boolean existsByEmail(@NonNull String email);

    @Query("SELECT u.email FROM BaseUser u WHERE u.id = :id")
    Optional<String> findEmailByUserId(@Param("id") @NonNull UUID userId);

    @Query("FROM BaseUser u WHERE u.createdDate < :date AND u.emailConfirmed = false")
    List<BaseUser> findUnverified(@Param("date") LocalDateTime utmostValidCreationDate);
}