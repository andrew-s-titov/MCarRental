package com.mcarrental.jwtauthservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcarrental.jwtauthservice.dto.ApiErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestUtil {

    public static final String ACCESS_DENIED_MESSAGE = "Access denied: not enough rights";
    public static final String UNAUTHORIZED_MESSAGE = "Not enough security info to authenticate user";
    public static final String UUID_V4_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
    public static final String UUID_V4_PATH = "/{id:" + UUID_V4_REGEX + "}";

    public static void writeErrorToResponse(HttpServletResponse response, ApiErrorDTO error, ObjectMapper objectMapper) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(error.getStatus().value());
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(error));
        writer.flush();
    }

    public static ApiErrorDTO default403Error(HttpServletRequest request) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(ACCESS_DENIED_MESSAGE)
                .path(request.getRequestURI())
                .build();
    }

    public static ApiErrorDTO default401Error(HttpServletRequest request) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(UNAUTHORIZED_MESSAGE)
                .path(request.getRequestURI())
                .build();
    }

    public static ApiErrorDTO apiError(HttpServletRequest request, HttpStatus status, String message) {
        return ApiErrorDTO.builder()
                .path(request.getRequestURI())
                .status(status)
                .message(message)
                .build();
    }

    public static String getRequestIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress != null) {
            if (ipAddress.contains(",")) {
                ipAddress = ipAddress.split(",")[0];
            }
            return ipAddress;
        }
        return request.getRemoteAddr();
    }
}