package com.mcarrental.carsearchservice.converter;

import com.mcarrental.carsearchservice.event.BookingCreateEvent;
import com.mcarrental.carsearchservice.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingConverter {

    public Booking fromBookingCreateEvent(BookingCreateEvent event) {
        return new Booking(event.getBookingId(), event.getStart(), event.getEnd());
    }
}