package com.mcarrental.bookingservice.service;

import com.mcarrental.bookingservice.dto.booking.BookingCompletionDetails;
import com.mcarrental.bookingservice.dto.booking.BookingCreateRequestDTO;
import com.mcarrental.bookingservice.dto.booking.BookingViewDTO;
import com.mcarrental.bookingservice.enums.BookingStatusFilter;
import com.mcarrental.bookingservice.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import java.util.UUID;

import static com.mcarrental.bookingservice.security.Role.Code.ADMIN_MAIN;
import static com.mcarrental.bookingservice.security.Role.Code.ADMIN_MANAGER;
import static com.mcarrental.bookingservice.security.Role.Code.CAR_OWNER;
import static com.mcarrental.bookingservice.security.Role.Code.CLIENT;

public interface BookingService {

    @Secured(CLIENT)
    BookingViewDTO create(BookingCreateRequestDTO createRequest);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER, CLIENT})
    BookingViewDTO getBookingById(UUID bookingId);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER})
    Page<BookingViewDTO> getCarBookings(UUID carId, BookingStatusFilter bookingStatusFilter, Pageable pageable);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CLIENT})
    Page<BookingViewDTO> getClientBookings(UUID clientId, BookingStatusFilter bookingStatusFilter, Pageable pageable);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER})
    void completeBooking(UUID bookingId, BookingCompletionDetails details);

    @Secured({ADMIN_MAIN, ADMIN_MANAGER, CAR_OWNER, CLIENT})
    void abort(UUID bookingId);

    void processPaymentStatus(UUID bookingId, PaymentStatus paymentStatus);

    boolean carHasActiveBookings(UUID carId);
}