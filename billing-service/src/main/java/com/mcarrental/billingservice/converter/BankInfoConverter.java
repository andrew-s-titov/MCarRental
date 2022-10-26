package com.mcarrental.billingservice.converter;

import com.mcarrental.billingservice.dto.bankinfo.BankInfoCreateRequestDTO;
import com.mcarrental.billingservice.dto.bankinfo.BankInfoViewDTO;
import com.mcarrental.billingservice.model.BankInfo;
import org.springframework.stereotype.Component;

@Component
public class BankInfoConverter {

    public BankInfo fromCreateRequest(BankInfoCreateRequestDTO createRequest) {
        return BankInfo.builder()
                .account(createRequest.getAccount())
                .swift(createRequest.getSwift())
                .main(createRequest.isMain())
                .ownerId(createRequest.getOwnerId())
                .build();
    }

    public BankInfoViewDTO toView(BankInfo info) {
        return BankInfoViewDTO.builder()
                .id(info.getId())
                .account(info.getAccount())
                .swift(info.getSwift())
                .main(info.isMain())
                .bankName(info.getSwift().getBankName())
                .build();
    }
}
