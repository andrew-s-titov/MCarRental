package com.mcarrental.bookingservice.controller;

import com.mcarrental.bookingservice.service.BookingService;
import com.mcarrental.bookingservice.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/private/booking")
public class BookingPrivateController {

    private final BookingService bookingService;

    @GetMapping("/car" + RestUtil.UUID_V4_PATH)
    public ResponseEntity<Boolean> carHasActiveBookings(@PathVariable("id") UUID carId) {
        return ResponseEntity.ok(bookingService.carHasActiveBookings(carId));
    }
}