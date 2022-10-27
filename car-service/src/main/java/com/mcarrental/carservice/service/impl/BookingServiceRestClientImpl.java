package com.mcarrental.carservice.service.impl;

import com.mcarrental.carservice.service.BookingServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BookingServiceRestClientImpl implements BookingServiceClient {

    private final RestTemplate restTemplate;

    @Value("${external.url.booking.car-has-active-bookings}")
    private String bookingServiceCarHasBookingsUrl;

    @Override
    public boolean carHasActiveBookings(UUID carId) {
        String uri = UriComponentsBuilder.fromHttpUrl(bookingServiceCarHasBookingsUrl)
                .pathSegment(carId.toString())
                .toUriString();
        return Boolean.TRUE.equals(restTemplate.getForObject(uri, boolean.class));
    }
}