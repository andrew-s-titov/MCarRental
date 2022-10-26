package com.mcarrental.billingservice.service.impl;

import com.mcarrental.billingservice.event.BookingCreatedEvent;
import com.mcarrental.billingservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @KafkaListener(topics = "${kafka.topic.booking.create}")
    public void processNewBooking(BookingCreatedEvent bookingCreatedEvent) {
        log.info("- - - - - Event found: new booking with id " + bookingCreatedEvent.getBookingId());
        log.info("- - - - - Starting payment process...");
    }
}