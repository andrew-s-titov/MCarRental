package com.mcarrental.carservice.security;

import com.mcarrental.carservice.exception.InsufficientRightsException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.UUID;

@Component
public class SpringJwtSecurityInfoManager implements SecurityInfoManager {

    @NonNull
    @Override
    public UUID getUserId() {
        Object principal = getAuthentication().getPrincipal();
        if (principal instanceof Jwt) {
            return UUID.fromString(((Jwt) principal).getSubject());
        }
        throw new IllegalStateException("Wrong Principal type found in Authentication object, expected - Jwt, got - " + principal);
    }

    @NonNull
    @Override
    public Role getUserRole() {
        Iterator<? extends GrantedAuthority> iterator = getAuthentication().getAuthorities().iterator();
        if (iterator.hasNext()) {
            String roleName = iterator.next().getAuthority();
            try {
                return Role.valueOf(roleName);
            } catch (IllegalArgumentException ex) {
                throw new IllegalStateException("Unknown role found in GrantedAuthority object: " + roleName);
            }
        }
        throw new IllegalStateException("No authorities found in the Authentication object");
    }

    @Override
    public void checkOwnerOrClientRights(UUID userId) {
        if (getUserRole().equals(Role.CAR_OWNER) || getUserRole().equals(Role.CLIENT)) {
            if (!getUserId().equals(userId)) {
                throw new InsufficientRightsException();
            }
        }
    }

    @Override
    public void checkSameUser(UUID expectedUserId) {
        checkIdEquals(expectedUserId);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private void checkIdEquals(UUID expectedUserId) {
        if (!getUserId().equals(expectedUserId)) {
            throw new InsufficientRightsException();
        }
    }
}