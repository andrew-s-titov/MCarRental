package com.mcarrental.userservice.model;

import com.mcarrental.userservice.enums.CompanyType;
import com.mcarrental.userservice.security.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@SuperBuilder
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@DiscriminatorValue(value = Role.Code.CAR_OWNER)
@Polymorphism(type = PolymorphismType.EXPLICIT)
public class CarOwner extends BaseUser {

    @Column(name = "reg_number", nullable = false)
    private String regNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "company_type", nullable = false)
    private CompanyType companyType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "legal_address", nullable = false)
    private String legalAddress;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;
}