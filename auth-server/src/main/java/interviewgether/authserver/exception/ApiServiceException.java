package interviewgether.authserver.exception;

import lombok.Getter;

@Getter
public class ApiServiceException extends RuntimeException{
    private final String message;
    private final String causedBy;

    public ApiServiceException(String message, String causedBy) {
        super(message);
        this.message = message;
        this.causedBy = causedBy;
    }

}
