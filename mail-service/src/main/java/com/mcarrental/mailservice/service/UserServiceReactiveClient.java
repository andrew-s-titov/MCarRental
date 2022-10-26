package com.mcarrental.mailservice.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserServiceReactiveClient {

    Mono<String> getUserEmail(UUID userId);
}
