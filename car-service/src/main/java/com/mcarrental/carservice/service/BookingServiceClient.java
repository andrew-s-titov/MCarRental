package com.mcarrental.carservice.service;

import java.util.UUID;

public interface BookingServiceClient {

    boolean carHasActiveBookings(UUID carId);
}
