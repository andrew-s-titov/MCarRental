package com.mcarrental.carsearchservice.event;

import java.util.UUID;

public interface BookingEvent {

    UUID getBookingId();

    UUID getCarId();
}