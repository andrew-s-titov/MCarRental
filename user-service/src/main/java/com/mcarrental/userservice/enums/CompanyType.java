package com.mcarrental.userservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum CompanyType implements LocalizedEnum {
    LLC("legal-entity.llc"),
    ALC("legal-entity.alc"),
    PE("legal-entity.pe"),
    UE("legal-entity.ue"),
    PUBLIC_JSC("legal-entity.public-jsc"),
    PRIVATE_JSC("legal-entity.private-jsc"),
    IE("legal-entity.ie");

    private final String bundleResourceKey;

    CompanyType(String bundleResourceKey) {
        this.bundleResourceKey = bundleResourceKey;
    }

    @JsonCreator
    public static CompanyType fromString(String string) {
        for (CompanyType enumType : CompanyType.values()) {
            if (enumType.name().equalsIgnoreCase(string)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException(
                String.format("No enum constant %s for '%s'.", CompanyType.class.getCanonicalName(), string));
    }
}