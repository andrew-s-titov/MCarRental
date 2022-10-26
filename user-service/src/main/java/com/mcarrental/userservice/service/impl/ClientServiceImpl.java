package com.mcarrental.userservice.service.impl;

import com.mcarrental.userservice.converter.ClientConverter;
import com.mcarrental.userservice.dto.client.ClientCreateRequestDTO;
import com.mcarrental.userservice.dto.client.ClientUpdateRequestDTO;
import com.mcarrental.userservice.dto.client.ClientViewDTO;
import com.mcarrental.userservice.event.EmailVerificationCreatedEvent;
import com.mcarrental.userservice.model.Client;
import com.mcarrental.userservice.model.EmailVerification;
import com.mcarrental.userservice.repository.BaseUserRepository;
import com.mcarrental.userservice.repository.EmailVerificationRepository;
import com.mcarrental.userservice.repository.UserRepository;
import com.mcarrental.userservice.security.SecurityInfoManager;
import com.mcarrental.userservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final UserRepository<Client> clientRepository;
    private final BaseUserRepository baseUserRepository;
    private final ClientConverter converter;
    private final PasswordEncoder passwordEncoder;
    private final SecurityInfoManager securityInfoManager;
    private final EmailVerificationRepository emailVerificationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${security.email-verification.code.length}")
    private int codeLength;

    @Transactional
    @Override
    public ClientViewDTO create(ClientCreateRequestDTO createRequest) {
        String email = createRequest.getEmail();
        checkEmailUniqueness(email);
        Client newClient = converter.fromCreateRequest(createRequest);
        newClient.setPassword(passwordEncoder.encode(createRequest.getPassword()));
        createRequest.setPassword("");
        newClient = clientRepository.save(newClient);
        EmailVerification emailVerification = EmailVerification.create(newClient, codeLength);
        emailVerification = emailVerificationRepository.save(emailVerification);

        eventPublisher.publishEvent(new EmailVerificationCreatedEvent(email, emailVerification.getCode()));

        return converter.toView(clientRepository.save(newClient));
    }

    @Override
    public ClientViewDTO get(UUID clientId) {
        return converter.toView(safeGetClient(clientId));
    }

    @Transactional
    @Override
    public ClientViewDTO update(UUID clientId, ClientUpdateRequestDTO updateRequest) {
        Client client = safeGetClient(clientId);
        securityInfoManager.checkSameUser(clientId);
        boolean hasNewData = mergeClientWithUpdate(client, updateRequest);
        if (hasNewData) {
            clientRepository.save(client);
        }
        return converter.toView(client);
    }

    private Client safeGetClient(UUID id) {
        return clientRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Client with id " + id + " not found"));
    }

    private void checkEmailUniqueness(@NonNull String email) {
        if (baseUserRepository.existsByEmail(email)) {
            throw new EntityExistsException("Client with email '" + email + "' is already registered");
        }
    }

    private boolean mergeClientWithUpdate(Client client, ClientUpdateRequestDTO updateRequest) {
        boolean hasNewData = false;
        String newFirstName = updateRequest.getFirstName();
        if (StringUtils.isNotBlank(newFirstName) && !newFirstName.equals(client.getFirstName())) {
            client.setFirstName(newFirstName);
            hasNewData = true;
        }
        String newLastName = updateRequest.getLastName();
        if (StringUtils.isNotBlank(newLastName) && !newLastName.equals(client.getLastName())) {
            client.setLastName(newLastName);
            hasNewData = true;
        }
        String newPhone = updateRequest.getPhone();
        if (StringUtils.isNotBlank(newPhone) && !newPhone.equals(client.getPhone())) {
            client.setPhone(newPhone);
            hasNewData = true;
        }
        return hasNewData;
    }
}