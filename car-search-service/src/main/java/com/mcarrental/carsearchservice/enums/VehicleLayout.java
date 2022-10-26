package com.mcarrental.carsearchservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum VehicleLayout implements EnumWithPrettyName {
    FWD("FWD"),
    RWD("RWD"),
    AWD("4WD(4x4)");

    private final String prettyName;

    VehicleLayout(String prettyName) {
        this.prettyName = prettyName;
    }

    @JsonCreator
    public static VehicleLayout fromString(String string) {
        for (VehicleLayout enumType : VehicleLayout.values()) {
            if (enumType.name().equalsIgnoreCase(string)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException(
                String.format("No enum constant %s for '%s'.", VehicleLayout.class.getCanonicalName(), string));
    }
}
