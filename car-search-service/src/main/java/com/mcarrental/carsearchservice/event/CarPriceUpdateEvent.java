package com.mcarrental.carsearchservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarPriceUpdateEvent implements CarEvent {

    private UUID carId;

    private Integer pricePerDay;
}