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
public class BookingApproveEvent implements BookingEvent, LocalizedEvent {

    private UUID bookingId;

    private UUID carId;

    private UUID clientId;

    private UUID carOwnerId;

    private Long totalPrice;

    private LocalDateTime end;

    private Locale locale;

    public static BookingApproveEvent fromBooking(Booking booking) {
        return BookingApproveEvent.builder()
                .bookingId(booking.getId())
                .carId(booking.getCarId())
                .clientId(booking.getClientId())
                .carOwnerId(booking.getCarOwnerId())
                .totalPrice(booking.getTotalPrice())
                .end(booking.getEnd())
                .build();
    }
}