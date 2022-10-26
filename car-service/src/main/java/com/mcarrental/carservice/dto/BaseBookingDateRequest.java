package com.mcarrental.carservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.carservice.enums.BookTime;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class BaseBookingDateRequest {

    @JsonIgnore
    private final static int MIN_BOOKING_HOURS = 12;
    @JsonIgnore
    private final static int MIN_HOURS_TILL_BOOKING = 1;

    @JsonIgnore
    private LocalDateTime start;
    @JsonIgnore
    private LocalDateTime end;

    @Setter
    @FutureOrPresent
    @NotNull
    private LocalDate startDate;

    @Setter
    @NotNull
    BookTime startTime;

    @Setter
    @FutureOrPresent
    @NotNull
    @Range
    private LocalDate endDate;

    @Setter
    @NotNull
    BookTime endTime;

    @JsonIgnore
    @AssertTrue(message = "Booking is available at least " + MIN_HOURS_TILL_BOOKING + " hour(s) in advance " +
            "and at least for " + MIN_BOOKING_HOURS + " hours")
    public boolean isValidRangeAndEnoughTime() {
        if (ObjectUtils.allNotNull(startDate, startTime, endDate, endTime)) {
            setStartIfNull();
            setEndIfNull();
            return start.minusHours(MIN_HOURS_TILL_BOOKING).isAfter(LocalDateTime.now())
                    && end.compareTo(start) >= 0
                    && ChronoUnit.HOURS.between(start, end) >= MIN_BOOKING_HOURS;
        } else {
            return true;
        }
    }

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

    @Override
    public String toString() {
        return "start: " + start + ", end: " + end;
    }
}