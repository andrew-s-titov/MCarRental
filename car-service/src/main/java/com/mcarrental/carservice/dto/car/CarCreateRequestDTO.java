package com.mcarrental.carservice.dto.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mcarrental.carservice.enums.Brand;
import com.mcarrental.carservice.enums.CarType;
import com.mcarrental.carservice.enums.Fuel;
import com.mcarrental.carservice.enums.GearBox;
import com.mcarrental.carservice.enums.VehicleLayout;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.Year;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarCreateRequestDTO {

    @NotNull(message = "{validation.car.type.not-null}")
    private CarType type;

    @NotNull(message = "{validation.car.fuel-not-null}")
    private Fuel fuel;

    @NotNull(message = "{validation.car.gear.not-null}")
    private GearBox gearBox;

    @NotNull(message = "{validation.car.brand.not-null}")
    private Brand brand;

    @NotBlank(message = "{validation.car.model.not-blank}")
    private String model;

    @PastOrPresent(message = "{validation.car.prod-year}")
    private Year productionYear;

    @DecimalMin(value = "1.0", message = "{validation.engine.capacity.min}")
    @DecimalMax(value = "6.0", message = "{validation.engine.capacity.max}")
    private Double engineCapacity;

    @Min(value = 2, message = "{validation.car.seats.min}")
    private Integer numberOfSeats;

    @NotNull(message = "{validation.car.layout.not-null}")
    private VehicleLayout layout;

    @Positive(message = "{validation.car.price.positive}")
    private Integer pricePerDay;

    @NotBlank(message = "{validation.car.number.not-blank}")
    @Pattern(regexp = "^[0-9]{4}[ABCEHIKMOPTXY]{2}[0-7]$", message = "{validation.car.number.pattern}")
    private String numberPlate;

    @NotBlank(message = "{validation.car.vin.not-blank}")
    @Pattern(regexp = "^[0-9A-Z]{17}$", message = "{validation.car.vin.pattern}")
    private String vin;

    @JsonIgnore
    @Min(value = 2000, message = "{validation.car.prod-year.min}")
    private int productionYear() {
        return productionYear.getValue();
    }
}