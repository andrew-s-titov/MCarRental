package com.mcarrental.jwtauthservice.exception.handling;

import com.mcarrental.jwtauthservice.dto.ApiErrorDTO;
import com.mcarrental.jwtauthservice.util.RestUtil;
import com.mcarrental.jwtauthservice.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalRestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class, InvalidRequestException.class})
    public ApiErrorDTO handleBadRequestWithoutDetails(Exception ex, HttpServletRequest request) {
        String message = "Bad request";
        if (ex instanceof HttpMessageNotReadableException) message = "Missing or invalid request body";
        if (ex instanceof InvalidRequestException) message = ex.getMessage();
        return RestUtil.apiError(request, HttpStatus.BAD_REQUEST, message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiErrorDTO handleValidationErrors(BindException ex, HttpServletRequest request) {
        ApiErrorDTO error = RestUtil.apiError(request, HttpStatus.BAD_REQUEST, "Request body validation failed");
        error.setDetails(bindingErrorsToMap(ex));
        return error;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtValidationException.class)
    public ApiErrorDTO unauthorized(HttpServletRequest request, Exception ex) {
        return RestUtil.apiError(request, HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiErrorDTO insufficientRights(HttpServletRequest request, Exception ex) {
        if (ex instanceof AccessDeniedException) {
            return RestUtil.default403Error(request);
        }
        return RestUtil.apiError(request, HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiErrorDTO nonFound(HttpServletRequest request, Exception ex) {
        String message = "Resource not found";
        if (ex instanceof NoHandlerFoundException) message = "No handler found for requested URL";
        return RestUtil.apiError(request, HttpStatus.NOT_FOUND, message);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiErrorDTO wrongMethod(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        return RestUtil.apiError(request, HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiErrorDTO internalError(HttpServletRequest request, Exception ex) {
        ApiErrorDTO error = RestUtil.apiError(request, HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error");
        log.error("Unexpected error. ID: " + error.getErrorId(), ex);
        return error;
    }

    private Map<String, Object> bindingErrorsToMap(BindException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        if (ex.hasErrors()) {
            details.put("errors_total", String.valueOf(ex.getErrorCount()));
            if (ex.hasFieldErrors()) {
                details.put("field_errors", String.valueOf(ex.getFieldErrorCount()));
                details.put("errors", ex.getFieldErrors().stream()
                        .map(this::fieldErrorToDetails)
                        .collect(Collectors.toList()));
            }
        }
        return details;
    }

    private Map<String, Object> fieldErrorToDetails(FieldError error) {
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("field", error.getField());
        errorDetails.put("value", error.getRejectedValue());
        errorDetails.put("message", error.getDefaultMessage());
        return errorDetails;
    }
}
