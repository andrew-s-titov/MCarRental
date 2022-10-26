package com.mcarrental.billingservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreatedEvent {

    private UUID bookingId;

    private UUID carId;

    private UUID carOwnerId;

    private Long totalPrice;

    private String shortCarInfo;
}
