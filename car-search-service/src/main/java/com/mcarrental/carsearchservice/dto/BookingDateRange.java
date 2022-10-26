package com.mcarrental.carsearchservice.dto;

import com.mcarrental.carsearchservice.enums.BookTime;

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
