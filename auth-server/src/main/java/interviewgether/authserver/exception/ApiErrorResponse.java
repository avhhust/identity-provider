package interviewgether.authserver.exception;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Map;

// This class serves as a template for displaying exception for client
// Exceptions are mapped to ApiErrorResponse by ExceptionHandler
@Getter
public class ApiErrorResponse {
    private final OffsetDateTime timestamp;
    private final int statusCode;
    private final String status;
    private final String message;
    private final Map<String, String> details;

    public ApiErrorResponse(int statusCode, String status, String message, Map<String, String> details) {
        this.timestamp = OffsetDateTime.now();
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.details = details;
    }
}
