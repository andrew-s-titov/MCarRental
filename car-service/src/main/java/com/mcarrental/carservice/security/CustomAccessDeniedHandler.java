package com.mcarrental.carservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcarrental.carservice.dto.ApiErrorDTO;
import com.mcarrental.carservice.util.RestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
        ApiErrorDTO error = RestUtil.custom403Error(request);
        log.debug("Security schema violation: not enough rights. ID: {}", error.getErrorId(), ex);
        RestUtil.writeErrorToResponse(response, error, objectMapper);
    }
}
