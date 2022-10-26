package com.mcarrental.bookingservice.controller;

import com.mcarrental.bookingservice.dto.ItemsDTO;
import com.mcarrental.bookingservice.dto.booking.BookingCompletionDetails;
import com.mcarrental.bookingservice.dto.booking.BookingCreateRequestDTO;
import com.mcarrental.bookingservice.dto.booking.BookingViewDTO;
import com.mcarrental.bookingservice.enums.BookingStatusFilter;
import com.mcarrental.bookingservice.enums.PaymentStatus;
import com.mcarrental.bookingservice.service.BookingService;
import com.mcarrental.bookingservice.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingViewDTO> book(@Valid @RequestBody BookingCreateRequestDTO bookingRequest) {
        BookingViewDTO booking = bookingService.create(bookingRequest);
        return ResponseEntity.created(URI.create("/booking/" + booking.getId())).body(booking);
    }

    @GetMapping(RestUtil.UUID_V4_PATH)
    public ResponseEntity<BookingViewDTO> showBooking(@PathVariable("id") UUID bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }

    @GetMapping("/client" + RestUtil.UUID_V4_PATH)
    public ResponseEntity<ItemsDTO<BookingViewDTO>> showClientBookings(
            @PathVariable("id") UUID clientId,
            @RequestParam(value = "status", required = false) BookingStatusFilter status,
            Pageable pageable) {
        var bookingsPage = bookingService.getClientBookings(clientId, status, pageable);
        return ResponseEntity.ok(ItemsDTO.fromPage(bookingsPage));
    }

    @GetMapping("/car" + RestUtil.UUID_V4_PATH)
    public ResponseEntity<ItemsDTO<BookingViewDTO>> showCarBookings(
            @PathVariable("id") UUID carId,
            @RequestParam(value = "status", required = false) BookingStatusFilter status,
            Pageable pageable) {
        var bookingsPage = bookingService.getCarBookings(carId, status, pageable);
        return ResponseEntity.ok(ItemsDTO.fromPage(bookingsPage));
    }

    @PutMapping(RestUtil.UUID_V4_PATH + "/abort")
    public void abort(@PathVariable("id") UUID id) {
        bookingService.abort(id);
    }

    @PutMapping(RestUtil.UUID_V4_PATH + "/finish")
    public void reportBookingFinish(@PathVariable("id") UUID id, @RequestBody @Valid BookingCompletionDetails details) {
        bookingService.completeBooking(id, details);
    }

    @PutMapping(RestUtil.UUID_V4_PATH + "/payment")
    public void processPaymentStatus(@PathVariable("id") UUID bookingId, @RequestParam("status") PaymentStatus status) {
        bookingService.processPaymentStatus(bookingId, status);
    }
}