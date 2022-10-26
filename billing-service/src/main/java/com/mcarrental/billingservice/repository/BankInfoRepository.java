package com.mcarrental.billingservice.repository;

import com.mcarrental.billingservice.model.BankInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface BankInfoRepository extends JpaRepository<BankInfo, UUID> {

    List<BankInfo> findByOwnerId(@NonNull UUID ownerId);

    boolean existsByOwnerId(@NonNull UUID ownerId);

    boolean existsByOwnerIdAndAccount(@NonNull UUID ownerId, @NonNull String accountNumber);
}