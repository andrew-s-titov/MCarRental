package com.mcarrental.carsearchservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.carsearchservice.enums.Brand;
import com.mcarrental.carsearchservice.enums.CarType;
import com.mcarrental.carsearchservice.enums.Fuel;
import com.mcarrental.carsearchservice.enums.GearBox;
import com.mcarrental.carsearchservice.enums.VehicleLayout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarViewDTO {

    private UUID id;

    private CarType type;

    private Fuel fuel;

    private GearBox gearBox;

    private Brand brand;

    private String model;

    private Year productionYear;

    private Double engineCapacity;

    private Integer numberOfSeats;

    private VehicleLayout layout;

    private Integer pricePerDay;
}