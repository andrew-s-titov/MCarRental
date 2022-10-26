package com.mcarrental.bookingservice.dto.booking;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.bookingservice.dto.BaseBookingDateRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingCreateRequestDTO extends BaseBookingDateRequest {

    @NotNull(message = "{validation.booking.car-id.not-null}")
    private UUID carId;

    @NotNull(message = "{validation.booking.price.not-null}")
    private Long carPricePerDay;

    @NotNull(message = "{validation.booking.car-owner.not-null}")
    private UUID carOwnerId;
}