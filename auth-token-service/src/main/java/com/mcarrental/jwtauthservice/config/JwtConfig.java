package com.mcarrental.jwtauthservice.config;

import com.mcarrental.jwtauthservice.security.Role;
import com.mcarrental.jwtauthservice.security.TokenType;
import com.mcarrental.jwtauthservice.util.RestUtil;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class JwtConfig {

    @Value("${security.rsa.key.public}")
    private RSAPublicKey publicKey;
    @Value("${security.rsa.key.private}")
    private RSAPrivateKey privateKey;
    @Value("${security.jwt.claim.role}")
    private String roleClaimName = "role";
    @Value("${security.jwt.claim.type}")
    private String tokenTypeClaimName = "type";

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        final NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(this.publicKey).build();
        decoder.setJwtValidator(tokenValidator());
        return decoder;
    }

    private OAuth2TokenValidator<Jwt> tokenValidator() {
        return new DelegatingOAuth2TokenValidator<>(List.of(
                new JwtTimestampValidator(),
                roleClaimValidator(),
                typeClaimValidator(),
                subjectClaimUUIDValidator()));
    }

    private OAuth2TokenValidator<Jwt> roleClaimValidator() {
        return new JwtClaimValidator<String>(roleClaimName, role ->
            StringUtils.isNotBlank(role) && Arrays.stream(Role.values()).anyMatch(value -> role.equals(value.name()))
        );
    }

    private OAuth2TokenValidator<Jwt> typeClaimValidator() {
        return new JwtClaimValidator<String>(tokenTypeClaimName, type ->
                StringUtils.isNotBlank(type) && Arrays.stream(TokenType.values()).anyMatch(value -> type.equals(value.name()))
        );
    }

    private OAuth2TokenValidator<Jwt> subjectClaimUUIDValidator() {
        return token -> token.getSubject().matches(RestUtil.UUID_V4_REGEX) ?
                OAuth2TokenValidatorResult.success() :
                OAuth2TokenValidatorResult.failure(new OAuth2Error(
                        OAuth2ErrorCodes.INVALID_TOKEN,
                        "Token subject expected to be a valid UUID",
                        "https://tools.ietf.org/html/rfc6750#section-3.1"));
    }

    // spring-boot creates default JwtDecoder when boot 'issuer' or 'public key' properties are found
    // also spring-boot sets default filter chain when JwtDecoder bean is found (default or custom)
}
