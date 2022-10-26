package com.mcarrental.carservice.service;

import java.util.UUID;

public interface BookingServiceCaller {

    boolean carHasActiveBookings(UUID carId);
}
