package com.mcarrental.bookingservice.service.impl;

import com.mcarrental.bookingservice.converter.BookingConverter;
import com.mcarrental.bookingservice.dto.booking.BookingCompletionDetails;
import com.mcarrental.bookingservice.dto.booking.BookingCreateRequestDTO;
import com.mcarrental.bookingservice.dto.booking.BookingViewDTO;
import com.mcarrental.bookingservice.enums.BookingCompletion;
import com.mcarrental.bookingservice.enums.BookingStatus;
import com.mcarrental.bookingservice.enums.BookingStatusFilter;
import com.mcarrental.bookingservice.enums.PaymentStatus;
import com.mcarrental.bookingservice.event.BookingAbortEvent;
import com.mcarrental.bookingservice.event.BookingApproveEvent;
import com.mcarrental.bookingservice.event.BookingCreateEvent;
import com.mcarrental.bookingservice.event.BookingDeclineEvent;
import com.mcarrental.bookingservice.event.completion.BookingDamagedCompletionEvent;
import com.mcarrental.bookingservice.event.completion.BookingDelayedCompletionEvent;
import com.mcarrental.bookingservice.event.completion.BookingNormalCompletionEvent;
import com.mcarrental.bookingservice.exception.ConflictException;
import com.mcarrental.bookingservice.model.Booking;
import com.mcarrental.bookingservice.repository.BookingRepository;
import com.mcarrental.bookingservice.security.Role;
import com.mcarrental.bookingservice.security.SecurityInfoManager;
import com.mcarrental.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingConverter converter;
    private final SecurityInfoManager securityInfoManager;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public BookingViewDTO create(BookingCreateRequestDTO createRequest) {
        checkTimeOverlap(createRequest);
        var booking = converter.fromCreateRequest(createRequest);
        booking.setClientId(securityInfoManager.getUserId());
        booking.setStatus(BookingStatus.PENDING);
        booking = bookingRepository.save(booking);
        var bookingViewDTO = converter.toView(bookingRepository.save(booking));

        // event to start payment process & create calendar in car search service
        eventPublisher.publishEvent(BookingCreateEvent.fromBooking(booking));

        return bookingViewDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public BookingViewDTO getBookingById(UUID bookingId) {
        return converter.toView(safeGetBooking(bookingId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingViewDTO> getCarBookings(UUID carId, BookingStatusFilter bookingStatusFilter, Pageable pageable) {
        var validPageable = pageable == null ? Pageable.unpaged() : pageable;
        var bookingPage = bookingStatusFilter == null ? bookingRepository.findAllByCarId(carId, validPageable)
                : bookingRepository.findByCarIdAndStatusIn(carId, bookingStatusFilter.statuses, validPageable);
        var bookingsPageContent = bookingPage.getContent();
        if (!CollectionUtils.isEmpty(bookingsPageContent)) {
            securityInfoManager.checkOwnerOrClientRights(bookingsPageContent.get(0).getCarOwnerId());
        }
        return bookingPage.map(converter::toView);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingViewDTO> getClientBookings(UUID clientId, BookingStatusFilter bookingStatusFilter, Pageable pageable) {
        var validPageable = pageable == null ? Pageable.unpaged() : pageable;
        securityInfoManager.checkOwnerOrClientRights(clientId);
        var bookingPage = bookingStatusFilter == null ?
                bookingRepository.findAllByClientId(clientId, validPageable)
                : bookingRepository.findAllByClientIdAndStatusIn(clientId, bookingStatusFilter.statuses, validPageable);
        return bookingPage.map(converter::toView);
    }

    @Override
    @Transactional
    public void completeBooking(UUID bookingId, BookingCompletionDetails details) {
        var booking = safeGetBooking(bookingId);
        checkBookingCanBeFinished(booking);
        securityInfoManager.checkOwnerOrClientRights(booking.getCarOwnerId());
        var reportedFinishTime = details.getFinishTime();
        var finishTimestamp = reportedFinishTime == null ? LocalDateTime.now() : reportedFinishTime;
        booking.setFinishTime(finishTimestamp);
        booking.setStatus(BookingStatus.FINISHED);
        bookingRepository.save(booking);

        var completionType = details.getCompletionType();
        var isDelayed = finishTimestamp.isAfter(booking.getEnd());
        if (BookingCompletion.NORMAL.equals(completionType)) {
            if (isDelayed) {
                // TODO: process event for Client with email sending like "owner reported a delay, you have 3 days to
                // open a dispute, or you'll be charged for delay
                eventPublisher.publishEvent(BookingDelayedCompletionEvent.fromBooking(booking));
            } else {
                eventPublisher.publishEvent(BookingNormalCompletionEvent.fromBooking(booking));
            }
        }
        if (BookingCompletion.DAMAGED.equals(completionType)) {
            // TODO: process event for Client with email sending like "owner reported car damage, you have 7 days to
            // open a dispute, or you'll be charged with fine according to policy
            eventPublisher.publishEvent(BookingDamagedCompletionEvent.fromBooking(booking));
        }
    }

    @Override
    @Transactional
    public void abort(UUID bookingId) {
        // TODO: consequences: money return if less than X hours before?

        var booking = safeGetBooking(bookingId);
        checkBookingCanBeAborted(booking);
        checkRightsToAbort(booking);
        booking.setStatus(defineNewStatus());
        booking.setFinishTime(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        eventPublisher.publishEvent(BookingAbortEvent.fromBookingAndRole(booking, securityInfoManager.getUserRole()));
    }

    @Override
    @Transactional
    public void processPaymentStatus(UUID bookingId, PaymentStatus paymentStatus) {
        Booking booking = safeGetBooking(bookingId);
        checkPending(booking);
        switch (paymentStatus) {
            case SUCCESS: {
                booking.setStatus(BookingStatus.APPROVED);
                booking = bookingRepository.save(booking);
                eventPublisher.publishEvent(BookingApproveEvent.fromBooking(booking));
                break;
            }
            case FAILURE: {
                bookingRepository.delete(booking);
                eventPublisher.publishEvent(BookingDeclineEvent.fromBooking(booking));
                break;
            }
        }
    }

    @Override
    public boolean carHasActiveBookings(UUID carId) {
        return bookingRepository.existsByCarIdAndStatusIn(carId, BookingStatusFilter.ACTIVE.statuses);
    }

    private Booking safeGetBooking(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Booking with id %s not found", bookingId)));
    }

    private void checkBookingCanBeFinished(Booking booking) {
        var bookingStatus = booking.getStatus();
        var bookingStartTime = booking.getStart();
        var now = LocalDateTime.now();
        if (!BookingStatus.APPROVED.equals(bookingStatus) || now.isBefore(bookingStartTime)) {
            throw new ConflictException("Only running booking may be aborted");
        }
    }

    private void checkBookingCanBeAborted(Booking booking) {
        var now = LocalDateTime.now();
        if (booking.getStart().isBefore(now) && booking.getEnd().isAfter(now)) {
            throw new ConflictException("Booking cannot be aborted when client uses the car");
        }
        var currentStatus = booking.getStatus();
        if (!BookingStatusFilter.ACTIVE.statuses.contains(currentStatus)) {
            throw new ConflictException("Only active booking may be aborted");
        }
    }

    private void checkTimeOverlap(BookingCreateRequestDTO createRequest) {
        boolean overlaps = bookingRepository.findByCarIdAndStatusIn(createRequest.getCarId(),
                        BookingStatusFilter.ACTIVE.statuses, Pageable.unpaged()).stream()
                .anyMatch(b -> b.getStart().compareTo(createRequest.getEnd()) < 0
                        && b.getEnd().compareTo(createRequest.getStart()) > 0);
        if (overlaps) {
            throw new ConflictException("Car is not available for this period of time");
        }
    }

    private BookingStatus defineNewStatus() {
        Role userRole = securityInfoManager.getUserRole();
        switch (userRole) {
            case CLIENT: return BookingStatus.CLIENT_ABORTED;
            case CAR_OWNER: return BookingStatus.OWNER_ABORTED;
            default: return BookingStatus.ADMIN_ABORTED;
        }
    }

    private void checkRightsToAbort(Booking booking) {
        Role role = securityInfoManager.getUserRole();
        UUID expectedUserId = null;
        if (Role.CLIENT.equals(role)) {
            expectedUserId = booking.getClientId();
        }
        if (Role.CAR_OWNER.equals(role)) {
            expectedUserId = booking.getCarOwnerId();
        }
        if (expectedUserId != null) {
            securityInfoManager.checkOwnerOrClientRights(expectedUserId);
        }
    }

    private void checkPending(Booking booking) {
        if (!BookingStatus.PENDING.equals(booking.getStatus())) {
            throw new ConflictException("Only bookings with PENDING status can be approved or declined");
        }
    }
}
