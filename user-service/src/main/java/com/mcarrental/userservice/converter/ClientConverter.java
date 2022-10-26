package com.mcarrental.userservice.converter;

import com.mcarrental.userservice.dto.client.ClientCreateRequestDTO;
import com.mcarrental.userservice.dto.client.ClientViewDTO;
import com.mcarrental.userservice.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientConverter {

    public Client fromCreateRequest(ClientCreateRequestDTO createRequest) {
        return Client.builder()
                .email(createRequest.getEmail())
                .firstName(createRequest.getFirstName())
                .lastName(createRequest.getLastName())
                .phone(createRequest.getPhone())
                .active(true)
                .emailConfirmed(false)
                .build();
    }

    public ClientViewDTO toView(Client client) {
        return ClientViewDTO.builder()
                .id(client.getId())
                .email(client.getEmail())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phone(client.getPhone())
                .build();
    }
}
