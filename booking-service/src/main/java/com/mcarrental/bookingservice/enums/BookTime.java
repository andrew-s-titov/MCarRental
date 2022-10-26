package com.mcarrental.bookingservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public enum BookTime implements EnumWithPrettyName {
    EIGHT("08:00"),
    EIGHT_THIRTY("08:00"),
    NINE("09:00"),
    NINE_THIRTY("09:30"),
    TEN("10:00"),
    TEN_THIRTY("10:30"),
    ELEVEN("11:00"),
    ELEVEN_THIRTY("11:30"),
    TWELVE("12:00"),
    TWELVE_THIRTY("12:30"),
    THIRTEEN("13:00"),
    THIRTEEN_THIRTY("13:30"),
    FOURTEEN("14:00"),
    FOURTEEN_THIRTY("14:30"),
    FIFTEEN("15:00"),
    FIFTEEN_THIRTY("15:30"),
    SIXTEEN("16:00"),
    SIXTEEN_THIRTY("16:30"),
    SEVENTEEN("17:00"),
    SEVENTEEN_THIRTY("17:30"),
    EIGHTEEN("18:00"),
    EIGHTEEN_THIRTY("18:30"),
    NINETEEN("19:00"),
    NINETEEN_THIRTY("19:30"),
    TWENTY("20:00"),
    TWENTY_THIRTY("20:30"),
    TWENTY_ONE("21:00"),
    TWENTY_ONE_THIRTY("21:30"),
    TWENTY_TWO("22:00"),
    TWENTY_TWO_THIRTY("22:30"),
    TWENTY_THREE("23:00"),
    TWENTY_THREE_THIRTY("23:30");

    private final String prettyName;
    private final LocalTime time;

    BookTime(String prettyName) {
        this.prettyName = prettyName;
        this.time = LocalTime.parse(prettyName);
    }

    @JsonCreator
    public static BookTime fromString(String string) {
        for (BookTime enumType : BookTime.values()) {
            if (enumType.getPrettyName().equalsIgnoreCase(string) || enumType.name().equalsIgnoreCase(string)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException(
                String.format("No enum constant %s for '%s'.", BookTime.class.getCanonicalName(), string));
    }
}
