package com.mcarrental.bookingservice.event;

import com.mcarrental.bookingservice.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDeclineEvent implements BookingEvent, LocalizedEvent {

    private UUID bookingId;
    
    private UUID carId;

    private UUID clientId;

    private Long totalPrice;

    private UUID carOwnerId;

    private LocalDateTime start;

    private LocalDateTime end;

    private Locale locale;

    public static BookingDeclineEvent fromBooking(Booking booking) {
        return BookingDeclineEvent.builder()
                .bookingId(booking.getId())
                .carId(booking.getCarId())
                .clientId(booking.getClientId())
                .totalPrice(booking.getTotalPrice())
                .carOwnerId(booking.getCarOwnerId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}