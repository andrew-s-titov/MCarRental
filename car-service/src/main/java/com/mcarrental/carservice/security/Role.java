package com.mcarrental.carservice.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    CAR_OWNER(Code.CAR_OWNER),
    CLIENT(Code.CLIENT),
    ADMIN_MAIN(Code.ADMIN_MAIN),
    ADMIN_MANAGER(Code.ADMIN_MANAGER);

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public static class Code {
        public static final String CAR_OWNER = "CAR_OWNER";
        public static final String CLIENT = "CLIENT";
        public static final String ADMIN_MAIN = "ADMIN_MAIN";
        public static final String ADMIN_MANAGER = "ADMIN_MANAGER";

        public static final String[] NON_CLIENT_ROLES = {CAR_OWNER, ADMIN_MAIN, ADMIN_MANAGER};
        public static final String[] ANY_ADMIN_ROLE = {ADMIN_MAIN, ADMIN_MANAGER};
    }
}
