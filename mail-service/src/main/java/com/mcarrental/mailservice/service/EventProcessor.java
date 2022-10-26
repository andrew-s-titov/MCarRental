package com.mcarrental.mailservice.service;

import com.mcarrental.mailservice.event.booking.BookingAbortEvent;
import com.mcarrental.mailservice.event.booking.BookingApproveEvent;
import com.mcarrental.mailservice.event.user.AuthEvent;

public interface EventProcessor {

    void processBookingApprove(BookingApproveEvent event);

    void processBookingAbort(BookingAbortEvent event);

    void processEmailVerification(AuthEvent event);

    void processPasswordReset(AuthEvent event);
}
