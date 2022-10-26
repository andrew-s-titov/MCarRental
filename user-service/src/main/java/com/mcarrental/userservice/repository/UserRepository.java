package com.mcarrental.userservice.repository;

import com.mcarrental.userservice.model.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository<T extends BaseUser> extends JpaRepository<T, UUID> {

    Optional<T> findByEmail(@NonNull String email);
}