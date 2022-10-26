package com.mcarrental.carsearchservice.validation;

import com.mcarrental.carsearchservice.dto.BookingDateRange;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Component
public class BookingDateRangeValidator implements ConstraintValidator<ValidBookingDateRange, BookingDateRange> {

    private final MessageSource messageSource;

    @Value("${booking.min-range-hours}")
    private Integer defaultMinBookingHours;
    @Value("${booking.min-advance-hours}")
    private Integer defaultMinHoursTillBooking;

    private int minBookingHours;
    private int minHoursTillBooking;

    @Override
    public void initialize(ValidBookingDateRange annotation) {
        int constraintMinBookingHours = annotation.minBookingHours();
        minBookingHours = constraintMinBookingHours <= 0 ? defaultMinBookingHours : constraintMinBookingHours;

        int constraintMinHoursTillBooking = annotation.minHoursTillBooking();
        minHoursTillBooking = constraintMinHoursTillBooking <= 0 ? defaultMinHoursTillBooking : constraintMinHoursTillBooking;
    }

    @Override
    public boolean isValid(BookingDateRange value, ConstraintValidatorContext context) {
        if (ObjectUtils.allNotNull(value.getStartDate(), value.getStartTime(), value.getEndDate(), value.getEndTime())) {
            var now = LocalDateTime.now();
            var startTime = value.getStart();
            var endTime = value.getEnd();

            boolean validStart = startTime.compareTo(now) >= 0
                    && ChronoUnit.HOURS.between(now, startTime) >= minHoursTillBooking;
            boolean validEnd = endTime.compareTo(startTime) >= 0
                    && ChronoUnit.HOURS.between(startTime, endTime) >= minBookingHours;
            boolean validRange = validStart && validEnd;
            if (!validRange) {
                context.disableDefaultConstraintViolation();
                var currentLocale = LocaleContextHolder.getLocale();
                if (!validStart) {
                    var validationMessage = messageSource.getMessage("validation.booking.start.valid",
                            new Object[]{minHoursTillBooking}, currentLocale);
                    context.buildConstraintViolationWithTemplate(validationMessage)
                            .addPropertyNode("startDate")
                            .addConstraintViolation();
                    context.buildConstraintViolationWithTemplate(validationMessage)
                            .addPropertyNode("startTime")
                            .addConstraintViolation();
                }
                if (!validEnd) {
                    var validationMessage = messageSource.getMessage("validation.booking.end.valid",
                            new Object[]{minBookingHours}, currentLocale);
                    context.buildConstraintViolationWithTemplate(validationMessage)
                            .addPropertyNode("endDate")
                            .addConstraintViolation();
                    context.buildConstraintViolationWithTemplate(validationMessage)
                            .addPropertyNode("endTime")
                            .addConstraintViolation();
                }
            }
            return validRange;
        } else {
            return true;
        }
    }
}