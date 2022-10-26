package com.mcarrental.mailservice.service.impl;

import com.mcarrental.mailservice.service.UserServiceReactiveClient;
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
public class UserServiceReactiveClientImpl implements UserServiceReactiveClient {

    private final WebClient webClient;

    @Value("${external.url.user-service.email-by-id}")
    private String userServiceEmailEndpoint;

    @Override
    public Mono<String> getUserEmail(UUID userId) {
        var uriForEmail = UriComponentsBuilder.fromHttpUrl(userServiceEmailEndpoint)
                .pathSegment(userId.toString())
                .build()
                .toUri();
        return webClient.get()
                .uri(uriForEmail)
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .bodyToMono(String.class);
    }
}
