package com.mcarrental.carsearchservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum GearBox implements LocalizedEnum {
    AT("car.gear.at"),
    MT("car.gear.mt"),
    AMT("car.gear.amt");

    private final String bundleResourceKey;

    GearBox(String bundleResourceKey) {
        this.bundleResourceKey = bundleResourceKey;
    }

    @JsonCreator
    public static GearBox fromString(String string) {
        for (GearBox enumType : GearBox.values()) {
            if (enumType.name().equalsIgnoreCase(string)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException(
                String.format("No enum constant %s for '%s'.", GearBox.class.getCanonicalName(), string));
    }
}
