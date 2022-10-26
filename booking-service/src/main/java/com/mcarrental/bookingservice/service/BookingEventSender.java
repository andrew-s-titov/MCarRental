package com.mcarrental.bookingservice.service;

import com.mcarrental.bookingservice.event.BookingAbortEvent;
import com.mcarrental.bookingservice.event.BookingApproveEvent;
import com.mcarrental.bookingservice.event.BookingCreateEvent;
import com.mcarrental.bookingservice.event.BookingDeclineEvent;
import com.mcarrental.bookingservice.event.completion.BookingDamagedCompletionEvent;
import com.mcarrental.bookingservice.event.completion.BookingDelayedCompletionEvent;
import com.mcarrental.bookingservice.event.completion.BookingNormalCompletionEvent;

public interface BookingEventSender {

    void sendCreateEvent(BookingCreateEvent event);

    void sendApproveEvent(BookingApproveEvent event);

    void sendDeclineEvent(BookingDeclineEvent event);

    void sendAbortEvent(BookingAbortEvent event);

    void sendEarlyCompletionEvent(BookingNormalCompletionEvent event);

    void sendDelayedCompletionEvent(BookingDelayedCompletionEvent event);

    void sendDamagedCompletionEvent(BookingDamagedCompletionEvent event);
}