package com.mcarrental.bookingservice.event;

import com.mcarrental.bookingservice.model.Booking;
import com.mcarrental.bookingservice.security.Role;
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
public class BookingAbortEvent implements BookingEvent, LocalizedEvent {

    private UUID bookingId;

    private UUID carId;

    private Role role;

    private UUID clientId;

    private UUID carOwnerId;

    private Long totalPrice;

    private LocalDateTime start;

    private LocalDateTime end;

    private LocalDateTime aborted;

    private Locale locale;

    public static BookingAbortEvent fromBookingAndRole(Booking booking, Role role) {
        return BookingAbortEvent.builder()
                .bookingId(booking.getId())
                .carId(booking.getCarId())
                .role(role)
                .clientId(booking.getClientId())
                .carOwnerId(booking.getCarOwnerId())
                .totalPrice(booking.getTotalPrice())
                .start(booking.getStart())
                .end(booking.getEnd())
                .aborted(booking.getFinishTime())
                .build();
    }
}