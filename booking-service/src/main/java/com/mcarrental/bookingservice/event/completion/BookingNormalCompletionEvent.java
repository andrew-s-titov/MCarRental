package com.mcarrental.bookingservice.event.completion;

import com.mcarrental.bookingservice.event.BookingEvent;
import com.mcarrental.bookingservice.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingNormalCompletionEvent implements BookingEvent {

    private UUID bookingId;

    private UUID carId;

    private LocalDateTime end;

    private LocalDateTime finishTime;

    public static BookingNormalCompletionEvent fromBooking(Booking booking) {
        return BookingNormalCompletionEvent.builder()
                .bookingId(booking.getId())
                .carId(booking.getCarId())
                .end(booking.getEnd())
                .finishTime(booking.getFinishTime())
                .build();
    }
}