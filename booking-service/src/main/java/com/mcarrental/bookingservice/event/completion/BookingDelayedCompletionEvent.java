package com.mcarrental.bookingservice.event.completion;

import com.mcarrental.bookingservice.event.BookingEvent;
import com.mcarrental.bookingservice.event.LocalizedEvent;
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
public class BookingDelayedCompletionEvent implements BookingEvent, LocalizedEvent {

    private UUID bookingId;

    private UUID carId;

    private UUID clientId;

    private UUID carOwnerId;

    private Long totalPrice;

    private LocalDateTime end;

    private LocalDateTime finishTime;

    private Locale locale;

    public static BookingDelayedCompletionEvent fromBooking(Booking booking) {
        return BookingDelayedCompletionEvent.builder()
                .bookingId(booking.getId())
                .carId(booking.getCarId())
                .clientId(booking.getClientId())
                .carOwnerId(booking.getCarOwnerId())
                .totalPrice(booking.getTotalPrice())
                .end(booking.getEnd())
                .finishTime(booking.getFinishTime())
                .build();
    }
}