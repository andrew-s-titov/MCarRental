package com.mcarrental.bookingservice.repository;

import com.mcarrental.bookingservice.enums.BookingStatus;
import com.mcarrental.bookingservice.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Page<Booking> findAllByClientId(UUID clientId, Pageable pageable);

    Page<Booking> findAllByClientIdAndStatusIn(UUID clientId, List<BookingStatus> statuses, Pageable pageable);

    Page<Booking> findAllByCarId(UUID carId, Pageable pageable);

    Page<Booking> findByCarIdAndStatusIn(UUID carId, List<BookingStatus> statuses, Pageable pageable);

    boolean existsByCarIdAndStatusIn(UUID carId, List<BookingStatus> statuses);
}