package com.mcarrental.bookingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.bookingservice.enums.BookTime;
import com.mcarrental.bookingservice.validation.ValidBookingDateRange;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ValidBookingDateRange
public abstract class BaseBookingDateRequest implements BookingDateRange {

    @JsonIgnore
    private LocalDateTime start;
    @JsonIgnore
    private LocalDateTime end;

    @Setter
    @Getter
    @FutureOrPresent(message = "{validation.booking.start.date.future}")
    @NotNull(message = "{validation.booking.start.date.not-null}")
    private LocalDate startDate;

    @Setter
    @Getter
    @NotNull(message = "{validation.booking.start.time.not-null}")
    BookTime startTime;

    @Setter
    @Getter
    @FutureOrPresent(message = "{validation.booking.end.date.future}")
    @NotNull(message = "{validation.booking.end.date.not-null}")
    private LocalDate endDate;

    @Setter
    @Getter
    @NotNull(message = "{validation.booking.end.time.not-null}")
    BookTime endTime;

    @JsonIgnore
    public LocalDateTime getStart() {
        setStartIfNull();
        return this.start;
    }

    @JsonIgnore
    public LocalDateTime getEnd() {
        setEndIfNull();
        return this.end;
    }

    @JsonIgnore
    private void setStartIfNull() {
        if (this.start == null) {
            this.start = LocalDateTime.of(startDate, startTime.getTime());
        }
    }

    @JsonIgnore
    private void setEndIfNull() {
        if (this.end == null) {
            this.end = LocalDateTime.of(endDate, endTime.getTime());
        }
    }
}