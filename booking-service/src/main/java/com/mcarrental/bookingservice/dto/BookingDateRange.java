package com.mcarrental.bookingservice.dto;

import com.mcarrental.bookingservice.enums.BookTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BookingDateRange {

    LocalDateTime getStart();

    LocalDateTime getEnd();

    LocalDate getStartDate();

    LocalDate getEndDate();

    BookTime getStartTime();

    BookTime getEndTime();
}
