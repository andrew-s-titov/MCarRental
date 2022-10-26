package com.mcarrental.bookingservice.event;

import com.mcarrental.bookingservice.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BookingCreateEvent implements BookingEvent {

    private UUID bookingId;

    private UUID carId;

    private UUID clientId;

    private Long totalPrice;

    private LocalDateTime start;

    private LocalDateTime end;

    public static BookingCreateEvent fromBooking(Booking booking) {
        return BookingCreateEvent.builder()
                .bookingId(booking.getId())
                .carId(booking.getCarId())
                .clientId(booking.getClientId())
                .totalPrice(booking.getTotalPrice())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}