package com.mcarrental.mailservice.event.booking;

import com.mcarrental.mailservice.enums.Role;
import com.mcarrental.mailservice.event.LocalizedEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingAbortEvent implements BookingEvent, LocalizedEvent {

    private UUID bookingId;

    private Role role;

    private UUID clientId;

    private UUID carOwnerId;

    private Locale locale;
}