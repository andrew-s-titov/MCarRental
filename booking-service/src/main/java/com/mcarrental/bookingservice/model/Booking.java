package com.mcarrental.bookingservice.model;

import com.mcarrental.bookingservice.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(schema = "public", name = "booking")
public class Booking {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(name = "car_id", nullable = false)
    private UUID carId;

    @Column(name = "car_owner_id", nullable = false)
    private UUID carOwnerId;

    @Column(name = "book_from", nullable = false)
    private LocalDateTime start;

    @Column(name = "book_till", nullable = false)
    private LocalDateTime end;

    @Column(name = "finish_time", nullable = true)
    private LocalDateTime finishTime;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }
}