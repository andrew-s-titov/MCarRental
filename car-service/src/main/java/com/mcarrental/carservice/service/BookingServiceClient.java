package com.mcarrental.carservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(name = "${external.booking-service.name}")
public interface BookingServiceClient {

    @RequestMapping(method = RequestMethod.GET, path = "${external.booking-service.url.car-has-active-bookings}")
    Boolean carHasActiveBookings(@PathVariable("carId") UUID carId);
}