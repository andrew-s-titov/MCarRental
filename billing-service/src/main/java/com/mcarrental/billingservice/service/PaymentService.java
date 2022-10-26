package com.mcarrental.billingservice.service;

import com.mcarrental.billingservice.event.BookingCreatedEvent;

public interface PaymentService {

    void processNewBooking(BookingCreatedEvent bookingCreatedEvent);
}
