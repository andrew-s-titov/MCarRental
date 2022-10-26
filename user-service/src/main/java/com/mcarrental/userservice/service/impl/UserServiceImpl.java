package com.mcarrental.userservice.service.impl;

import com.mcarrental.userservice.repository.BaseUserRepository;
import com.mcarrental.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserServiceImpl implements UserService {

    private final BaseUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public String getEmailById(UUID userId) {
        Assert.notNull(userId, "Can't load user with 'null' ID");
        return userRepository.findEmailByUserId(userId).orElseThrow(() ->
                new EntityNotFoundException("User with id " + userId + " not found"));
    }
}
