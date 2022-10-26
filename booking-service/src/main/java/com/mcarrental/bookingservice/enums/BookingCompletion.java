package com.mcarrental.bookingservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

public enum BookingCompletion implements LocalizedEnum {
    NORMAL("booking.completion.normal"),
    DAMAGED("booking.completion.damaged");

    @Getter
    private final String bundleResourceKey;

    BookingCompletion(String bundleResourceKey) {
        this.bundleResourceKey = bundleResourceKey;
    }

    @JsonCreator
    public static BookingCompletion fromString(String string) {
        for (BookingCompletion enumType : BookingCompletion.values()) {
            if (enumType.name().equalsIgnoreCase(string)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException(
                String.format("No enum constant %s for '%s'.", BookingCompletion.class.getCanonicalName(), string));
    }
}