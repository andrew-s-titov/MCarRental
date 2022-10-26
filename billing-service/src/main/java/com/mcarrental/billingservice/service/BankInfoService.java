package com.mcarrental.billingservice.service;

import com.mcarrental.billingservice.dto.bankinfo.BankInfoCreateRequestDTO;
import com.mcarrental.billingservice.dto.bankinfo.BankInfoViewDTO;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.UUID;

import static com.mcarrental.billingservice.security.Role.Code.ADMIN_MAIN;
import static com.mcarrental.billingservice.security.Role.Code.ADMIN_MANAGER;
import static com.mcarrental.billingservice.security.Role.Code.CAR_OWNER;

public interface BankInfoService {

    @Secured(CAR_OWNER)
    BankInfoViewDTO addBankInfo(BankInfoCreateRequestDTO bankInfoCreateRequest);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER})
    BankInfoViewDTO getBankInfo(UUID bankInfoId);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER})
    List<BankInfoViewDTO> getOwnerBankInfo(UUID carOwnerId);

    @Secured(CAR_OWNER)
    void setBankInfoAsMain(UUID bankInfoId);

    @Secured(CAR_OWNER)
    void removeBankInfo(UUID bankInfoId);
}