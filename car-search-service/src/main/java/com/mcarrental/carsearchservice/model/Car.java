package com.mcarrental.carsearchservice.model;

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
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Year;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "car")
public class Car {

    @Id
    @Field(type = FieldType.Keyword)
    private UUID carId;

    @Field(type = FieldType.Keyword)
    private CarType type;

    @Field(type = FieldType.Keyword)
    private Fuel fuel;

    @Field(type = FieldType.Keyword)
    private GearBox gearBox;

    @Field(type = FieldType.Keyword)
    private Brand brand;

    private String model;

    @Field(type = FieldType.Date, format = DateFormat.year)
    private Year productionYear;

    private Double engineCapacity;

    private Integer numberOfSeats;

    @Field(type = FieldType.Keyword)
    private VehicleLayout layout;

    private Integer pricePerDay;

    @Field(type = FieldType.Nested)
    private Set<Booking> bookings;

    public boolean addBooking(Booking booking) {
        if (bookings == null) {
            bookings = new HashSet<>();
        }
        return bookings.add(booking);
    }

    public boolean removeBooking(Booking booking) {
        if (bookings != null) {
            return bookings.remove(booking);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return carId.equals(car.carId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carId);
    }
}