package com.mcarrental.carsearchservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.carsearchservice.enums.Brand;
import com.mcarrental.carsearchservice.enums.CarType;
import com.mcarrental.carsearchservice.enums.Fuel;
import com.mcarrental.carsearchservice.enums.GearBox;
import com.mcarrental.carsearchservice.enums.VehicleLayout;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarSearchRequestDTO extends BaseBookingDateRequest {

    private List<CarType> types;

    private List<Fuel> fuelTypes;

    private List<GearBox> gearBoxTypes;

    private List<Brand> brands;

    private List<VehicleLayout> layouts;

    private Year productionYear;

    private Integer numberOfSeats;

    private Integer pricePerDay;
}