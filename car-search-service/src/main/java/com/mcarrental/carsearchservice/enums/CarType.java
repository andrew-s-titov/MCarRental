package com.mcarrental.carsearchservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum CarType implements LocalizedEnum {
    MINIVAN("car.type.minivan"),
    SEDAN("car.type.sedan"),
    HATCHBACK("car.type.hatchback"),
    WAGON("car.type.wagon");

    private final String bundleResourceKey;

    CarType(String bundleResourceKey) {
        this.bundleResourceKey = bundleResourceKey;
    }

    @JsonCreator
    public static CarType fromString(String string) {
        for (CarType enumType : CarType.values()) {
            if (enumType.name().equalsIgnoreCase(string)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException(
                String.format("No enum constant %s for '%s'.", CarType.class.getCanonicalName(), string));
    }
}
