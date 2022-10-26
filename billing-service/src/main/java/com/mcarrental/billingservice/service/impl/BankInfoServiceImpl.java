package com.mcarrental.billingservice.service.impl;

import com.mcarrental.billingservice.converter.BankInfoConverter;
import com.mcarrental.billingservice.dto.bankinfo.BankInfoCreateRequestDTO;
import com.mcarrental.billingservice.dto.bankinfo.BankInfoViewDTO;
import com.mcarrental.billingservice.exception.ConflictException;
import com.mcarrental.billingservice.model.BankInfo;
import com.mcarrental.billingservice.repository.BankInfoRepository;
import com.mcarrental.billingservice.security.SecurityInfoManager;
import com.mcarrental.billingservice.service.BankInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BankInfoServiceImpl implements BankInfoService {

    private final BankInfoRepository bankInfoRepository;
    private final BankInfoConverter converter;
    private final SecurityInfoManager securityInfoManager;

    @Override
    @Transactional(readOnly = true)
    public List<BankInfoViewDTO> getOwnerBankInfo(UUID id) {
        securityInfoManager.checkOwnerOrClientRights(id);
        return bankInfoRepository.findByOwnerId(id).stream()
                .map(converter::toView)
                .collect(Collectors.toList());
    }

    @Override
    public BankInfoViewDTO getBankInfo(UUID bankInfoId) {
        BankInfo bankInfo = safeGetBankInfo(bankInfoId);
        securityInfoManager.checkOwnerOrClientRights(bankInfo.getOwnerId());
        return converter.toView(bankInfo);
    }

    @Override
    @Transactional
    public BankInfoViewDTO addBankInfo(BankInfoCreateRequestDTO bankInfoCreateRequest) {
        checkBankInfoUniqueness(bankInfoCreateRequest);
        UUID ownerId = bankInfoCreateRequest.getOwnerId();
        securityInfoManager.checkSameUser(ownerId);
        BankInfo newBankInfo = converter.fromCreateRequest(bankInfoCreateRequest);
        if (newBankInfo.isMain()) {
            removeMainStatusFromAllBankInfo(ownerId);
        } else {
            setMainIfFirstBankInfo(newBankInfo);
        }
        newBankInfo = bankInfoRepository.save(newBankInfo);
        return converter.toView(newBankInfo);
    }

    @Override
    @Transactional
    public void setBankInfoAsMain(UUID bankInfoId) {
        BankInfo bankInfo = safeGetBankInfo(bankInfoId);
        if (bankInfo.isMain()) {
            return;
        }
        UUID ownerId = bankInfo.getOwnerId();
        securityInfoManager.checkSameUser(ownerId);
        removeMainStatusFromAllBankInfo(ownerId);
        bankInfo.setMain(true);
        bankInfoRepository.save(bankInfo);
    }

    @Override
    @Transactional
    public void removeBankInfo(UUID bankInfoId) {
        BankInfo bankInfo = safeGetBankInfo(bankInfoId);
        if (bankInfo.isMain()) {
            throw new ConflictException("Cannot remove main bank account");
        }
        securityInfoManager.checkSameUser(bankInfo.getOwnerId());
        bankInfoRepository.delete(bankInfo);
    }

    private BankInfo safeGetBankInfo(UUID bankInfoId) {
        return bankInfoRepository.findById(bankInfoId)
                .orElseThrow(() -> new EntityNotFoundException("Bank info with id " + bankInfoId + " not found"));
    }

    private void removeMainStatusFromAllBankInfo(UUID carOwner) {
        bankInfoRepository.findByOwnerId(carOwner).stream()
                        .filter(BankInfo::isMain)
                                .forEach(info -> {
                                    info.setMain(false);
                                    bankInfoRepository.save(info);
                                });
    }

    private void checkBankInfoUniqueness(BankInfoCreateRequestDTO bankInfoCreateRequest) {
        String account = bankInfoCreateRequest.getAccount();
        if (bankInfoRepository.existsByOwnerIdAndAccount(bankInfoCreateRequest.getOwnerId(), account)) {
            throw new EntityExistsException("Bank account " + account + " is already registered");
        }
    }

    private void setMainIfFirstBankInfo(BankInfo bankInfo) {
        if (!bankInfoRepository.existsByOwnerId(bankInfo.getOwnerId())) {
            bankInfo.setMain(true);
        }
    }
}
