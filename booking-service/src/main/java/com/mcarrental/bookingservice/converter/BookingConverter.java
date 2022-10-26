package com.mcarrental.bookingservice.converter;

import com.mcarrental.bookingservice.dto.booking.BookingCreateRequestDTO;
import com.mcarrental.bookingservice.dto.booking.BookingViewDTO;
import com.mcarrental.bookingservice.model.Booking;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class BookingConverter {

    public Booking fromCreateRequest(BookingCreateRequestDTO createRequest) {
        return Booking.builder()
                .start(createRequest.getStart())
                .end(createRequest.getEnd())
                .carId(createRequest.getCarId())
                .carOwnerId(createRequest.getCarOwnerId())
                .totalPrice(computeTotalPrice(createRequest))
                .build();
    }

    public BookingViewDTO toView(Booking booking) {
        return BookingViewDTO.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .clientId(booking.getClientId())
                .carId(booking.getCarId())
                .carOwnerId(booking.getCarOwnerId())
                .totalPrice(booking.getTotalPrice())
                .build();
    }

    private Long computeTotalPrice(BookingCreateRequestDTO createRequest) {
        long hoursToBook = Duration.between(createRequest.getStart(), createRequest.getEnd()).toHours();
        return (hoursToBook + 23) / 24 * createRequest.getCarPricePerDay();
    }
}