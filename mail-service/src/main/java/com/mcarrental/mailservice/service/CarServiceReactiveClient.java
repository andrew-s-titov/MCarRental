package com.mcarrental.mailservice.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CarServiceReactiveClient {

    Mono<String> getCarShortInfo(UUID carId);
}