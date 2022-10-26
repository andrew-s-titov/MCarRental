package com.mcarrental.carsearchservice.security;

import com.mcarrental.carsearchservice.util.RestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

@Configuration
public class JwtDecoderConfig {

    private final static String DEFAULT_TOKEN_TYPE = "ACCESS";
    private final static String JWT_ROLE_PREFIX = "";

    @Value("${security.rsa.key.public}")
    private RSAPublicKey publicKey;
    @Value("${security.jwt.claim.role}")
    private String roleClaim = "role";
    @Value("${security.jwt.claim.type}")
    private String tokenTypeClaim = "type";

    @Bean
    public JwtDecoder jwtDecoder() {
        final NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(this.publicKey).build();
        decoder.setJwtValidator(tokenValidator());
        return decoder;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName(roleClaim);
        grantedAuthoritiesConverter.setAuthorityPrefix(JWT_ROLE_PREFIX);

        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    private OAuth2TokenValidator<Jwt> tokenValidator() {
        return new DelegatingOAuth2TokenValidator<>(List.of(
                new JwtTimestampValidator(),
                roleClaimValidator(),
                typeClaimValidator(),
                subjectClaimUUIDValidator()));
    }

    private OAuth2TokenValidator<Jwt> roleClaimValidator() {
        return new JwtClaimValidator<String>(roleClaim, role ->
                StringUtils.isNotBlank(role) && Arrays.stream(Role.values()).anyMatch(value -> role.equals(value.name()))
        );
    }

    private OAuth2TokenValidator<Jwt> typeClaimValidator() {
        return new JwtClaimValidator<String>(tokenTypeClaim, type ->
                StringUtils.isNotBlank(type) && type.equals(DEFAULT_TOKEN_TYPE));
    }

    private OAuth2TokenValidator<Jwt> subjectClaimUUIDValidator() {
        return token -> token.getSubject().matches(RestUtil.UUID_V4_REGEX) ?
                OAuth2TokenValidatorResult.success() :
                OAuth2TokenValidatorResult.failure(new OAuth2Error(
                        OAuth2ErrorCodes.INVALID_TOKEN,
                        "Token subject expected to be a valid UUID",
                        "https://tools.ietf.org/html/rfc6750#section-3.1"));
    }
}
