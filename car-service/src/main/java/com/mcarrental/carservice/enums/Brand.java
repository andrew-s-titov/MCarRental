package com.mcarrental.carservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Brand implements EnumWithPrettyName {
    TOYOTA("Toyota"),
    HYUNDAI("Hyundai"),
    KIA("Kia"),
    VW("Volkswagen"),
    MERCEDES("Mercedes"),
    BMW("BMW"),
    MITSUBISHI("Mitsubishi"),
    FORD("Ford"),
    PEUGEOT("Peugeot"),
    RENAULT("Renault"),
    LADA("Lada");

    private final String prettyName;

    Brand(String prettyName) {
        this.prettyName = prettyName;
    }

    @JsonCreator
    public static Brand fromString(String string) {
        for (Brand enumType : Brand.values()) {
            if (enumType.getPrettyName().equalsIgnoreCase(string) || enumType.name().equalsIgnoreCase(string)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException(
                String.format("No enum constant %s for '%s'.", Brand.class.getCanonicalName(), string));
    }
}
