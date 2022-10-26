package com.mcarrental.carsearchservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCancelEvent implements BookingEvent {

    private UUID bookingId;

    private UUID carId;

    private LocalDateTime start;

    private LocalDateTime end;
}