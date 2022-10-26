package com.mcarrental.userservice.cronjob.impl;

import com.mcarrental.userservice.cronjob.DeleteUnverifiedUserJob;
import com.mcarrental.userservice.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Component
public class DeleteUnverifiedUserJobImpl implements DeleteUnverifiedUserJob {

    private final BaseUserRepository userRepository;

    @Value("${security.email-verification.max-hours}")
    private Integer maxUnverifiedHours;

    @Scheduled(cron = "${cronjob.user.delete-unverified}")
    @Override
    public void deleteUnverifiedUsers() {
        var now = LocalDateTime.now();
        var utmostValidCreationDate = now.minus(maxUnverifiedHours, ChronoUnit.HOURS);
        var expiredUnverifiedUsers = userRepository.findUnverified(utmostValidCreationDate);
        userRepository.deleteAll(expiredUnverifiedUsers);
    }
}