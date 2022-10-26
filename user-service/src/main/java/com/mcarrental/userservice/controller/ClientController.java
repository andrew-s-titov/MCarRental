package com.mcarrental.userservice.controller;

import com.mcarrental.userservice.dto.client.ClientCreateRequestDTO;
import com.mcarrental.userservice.dto.client.ClientUpdateRequestDTO;
import com.mcarrental.userservice.dto.client.ClientViewDTO;
import com.mcarrental.userservice.service.ClientService;
import com.mcarrental.userservice.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/client")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientViewDTO> create(@Valid @RequestBody ClientCreateRequestDTO createRequest) {
        ClientViewDTO client = clientService.create(createRequest);
        return ResponseEntity.created(URI.create("/client/" + client.getId())).body(client);
    }

    @GetMapping(RestUtil.UUID_V4_PATH)
    public ResponseEntity<ClientViewDTO> view(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(clientService.get(id));
    }

    @PutMapping(RestUtil.UUID_V4_PATH)
    public ResponseEntity<ClientViewDTO> update(@PathVariable("id") UUID id,
                                                @Valid @RequestBody ClientUpdateRequestDTO updateRequest) {
        return ResponseEntity.ok(clientService.update(id, updateRequest));
    }
}
