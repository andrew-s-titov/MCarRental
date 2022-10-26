package com.mcarrental.mailservice.event.booking;

import java.util.UUID;

public interface BookingEvent {

    UUID getBookingId();

    UUID getClientId();

    UUID getCarOwnerId();
}