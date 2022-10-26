package com.mcarrental.billingservice.dto.bankinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcarrental.billingservice.enums.SwiftCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankInfoCreateRequestDTO {

    @JsonProperty(value = "owner_id")
    @NotNull(message = "{validation.bank.owner.not-null}")
    private UUID ownerId;

    @NotBlank(message = "{validation.bank.account.not-blank}")
    @Pattern(regexp = "^BY\\d{2}[A-Z]{4}[0-9A-Z]{16}$", message = "{validation.bank.account.pattern}")
    private String account;

    @NotNull(message = "{validation.bank.swift.not-null}")
    private SwiftCode swift;

    @NotNull(message = "{validation.bank.main.not-null}")
    private boolean main;

    @JsonIgnore
    @AssertTrue(message = "{validation.bank.iban-matches}")
    public boolean isAccountBelongToBank() {
        return swift.name().startsWith(account.substring(4, 8));
    }
}
