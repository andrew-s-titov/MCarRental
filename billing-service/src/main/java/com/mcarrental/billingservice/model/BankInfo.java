package com.mcarrental.billingservice.model;

import com.mcarrental.billingservice.enums.SwiftCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(schema = "public", name = "bank_info")
public class BankInfo {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "account", nullable = false)
    private String account;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "swift", nullable = false)
    private SwiftCode swift;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "main", nullable = false)
    private boolean main;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankInfo bankInfo = (BankInfo) o;
        return id.equals(bankInfo.id) && account.equals(bankInfo.account) && swift == bankInfo.swift;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, swift);
    }
}
