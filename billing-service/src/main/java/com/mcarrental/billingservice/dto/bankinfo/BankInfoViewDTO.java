package com.mcarrental.billingservice.dto.bankinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcarrental.billingservice.enums.SwiftCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class BankInfoViewDTO {

    private UUID id;

    private String account;

    private SwiftCode swift;

    @JsonProperty("bank_name")
    private String bankName;

    private boolean main;
}