package com.mcarrental.carsearchservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Fuel implements LocalizedEnum {
    DIESEL("car.fuel.diesel"),
    GAS("car.fuel.gas"),
    PETROL("car.fuel.petrol"),
    HYBRID("car.fuel.hybrid");

    private final String bundleResourceKey;

    Fuel(String bundleResourceKey) {
        this.bundleResourceKey = bundleResourceKey;
    }

    @JsonCreator
    public static Fuel fromString(String string) {
        for (Fuel enumType : Fuel.values()) {
            if (enumType.name().equalsIgnoreCase(string)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException(
                String.format("No enum constant %s for '%s'.", Fuel.class.getCanonicalName(), string));
    }
}