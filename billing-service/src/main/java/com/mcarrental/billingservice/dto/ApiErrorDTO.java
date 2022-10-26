package com.mcarrental.billingservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiErrorDTO {

    private UUID errorId;

    private LocalDateTime timestamp;

    private HttpStatus status;

    private String message;

    private String path;

    @Setter
    private Map<String, Object> details;

    public static ApiErrorDTOBuilder builder() {
        return new ApiErrorDTOBuilder();
    }

    public void addDetail(String name, Object detail) {
        if (this.details == null) this.details = new LinkedHashMap<>();
        this.details.put(name, detail);
    }

    @JsonProperty("status")
    public String getStatusAsString() {
        return this.status.toString();
    }

    public ApiErrorDTO(HttpStatus status, String message, String path, Map<String, Object> details) {
        this.errorId = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
        this.details = details;
    }

    public static class ApiErrorDTOBuilder {
        private HttpStatus status;
        private String message;
        private String path;
        private Map<String, Object> details;

        ApiErrorDTOBuilder() {
        }

        public ApiErrorDTOBuilder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public ApiErrorDTOBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ApiErrorDTOBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ApiErrorDTOBuilder details(Map<String, Object> details) {
            this.details = details;
            return this;
        }

        public ApiErrorDTO build() {
            return new ApiErrorDTO(status, message, path, details);
        }

        public String toString() {
            return "ApiErrorDTO.ApiErrorDTOBuilder(status=" + this.status + ", message=" + this.message + ", path=" + this.path + ", details=" + this.details + ")";
        }
    }
}