package interviewgether.authserver.exception;

import java.time.OffsetDateTime;
import java.util.Map;

// This class serves as a template for displaying exception for client
// Exceptions are mapped to ApiErrorResponse by ExceptionHandler
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

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}
