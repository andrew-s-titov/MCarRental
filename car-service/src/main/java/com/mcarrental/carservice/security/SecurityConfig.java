package com.mcarrental.carservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

import static com.mcarrental.carservice.security.Role.Code.CAR_OWNER;
import static com.mcarrental.carservice.security.Role.Code.CLIENT;
import static com.mcarrental.carservice.security.Role.Code.NON_CLIENT_ROLES;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().cors().and().csrf().disable()

                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .authorizeRequests()

                .antMatchers(HttpMethod.GET, "/dictionary/**").permitAll()
                .antMatchers(HttpMethod.GET, "/car/{carId}/short").permitAll()
                .antMatchers(HttpMethod.POST, "/car").hasRole(CAR_OWNER)
                .antMatchers(HttpMethod.GET, "/car/search").hasRole(CLIENT)
                .antMatchers(HttpMethod.GET, "/car").hasAnyRole(NON_CLIENT_ROLES)
                .antMatchers(HttpMethod.GET, "/car/{carId}").hasAnyRole(NON_CLIENT_ROLES)
                .antMatchers(HttpMethod.GET, "/car/{carId}/for_client").hasRole(CLIENT)
                .antMatchers(HttpMethod.PUT, "/car/{carId}", "/car/{carId}/price").hasAnyRole(CAR_OWNER)
                .antMatchers(HttpMethod.PUT, "/car/{carId}/visibility").hasAnyRole(NON_CLIENT_ROLES)
                .antMatchers(HttpMethod.DELETE, "/car/{carId}").hasRole(CAR_OWNER)

                .anyRequest().authenticated()

                .and()
                .oauth2ResourceServer(oauth2Server -> {
                    oauth2Server.jwt();
                    oauth2Server.authenticationEntryPoint(authenticationEntryPoint);
                })

                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
