package com.mcarrental.bookingservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

import static com.mcarrental.bookingservice.enums.BookingStatus.ADMIN_ABORTED;
import static com.mcarrental.bookingservice.enums.BookingStatus.APPROVED;
import static com.mcarrental.bookingservice.enums.BookingStatus.CLIENT_ABORTED;
import static com.mcarrental.bookingservice.enums.BookingStatus.DECLINED;
import static com.mcarrental.bookingservice.enums.BookingStatus.FINISHED;
import static com.mcarrental.bookingservice.enums.BookingStatus.OWNER_ABORTED;
import static com.mcarrental.bookingservice.enums.BookingStatus.PENDING;

public enum BookingStatusFilter implements LocalizedEnum {
    ACTIVE("booking.status.active", List.of(PENDING, APPROVED)),
    NOT_ACTIVE("booking.status.not-active", List.of(DECLINED, CLIENT_ABORTED, OWNER_ABORTED, ADMIN_ABORTED, FINISHED));

    @Getter
    private final String bundleResourceKey;
    public final List<BookingStatus> statuses;

    BookingStatusFilter(String bundleResourceKey, List<BookingStatus> statuses) {
        this.bundleResourceKey = bundleResourceKey;
        this.statuses = statuses;
    }

    @JsonCreator
    public static BookingStatusFilter fromString(String string) {
        for (BookingStatusFilter enumType : BookingStatusFilter.values()) {
            if (enumType.name().equalsIgnoreCase(string)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException(
                String.format("No enum constant %s for '%s'.", BookingStatusFilter.class.getCanonicalName(), string));
    }
}