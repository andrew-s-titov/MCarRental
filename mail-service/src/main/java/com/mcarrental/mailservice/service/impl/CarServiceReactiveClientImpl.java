package com.mcarrental.mailservice.service.impl;

import com.mcarrental.mailservice.service.CarServiceReactiveClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CarServiceReactiveClientImpl implements CarServiceReactiveClient {

    private final WebClient webClient;

    @Value("${external.url.car-service.short-info}")
    private String carShortInfoEndpoint;

    @Override
    public Mono<String> getCarShortInfo(UUID carId) {
        var uriForInfo = UriComponentsBuilder.fromHttpUrl(carShortInfoEndpoint)
                .buildAndExpand(carId)
                .toUri();
        return webClient.get()
                .uri(uriForInfo)
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .bodyToMono(String.class);
    }
}
