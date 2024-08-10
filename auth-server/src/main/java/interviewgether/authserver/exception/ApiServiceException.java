package interviewgether.authserver.exception;

public class ApiServiceException extends RuntimeException{
    private final String message;
    private final String causedBy;

    public ApiServiceException(String message, String causedBy) {
        super(message);
        this.message = message;
        this.causedBy = causedBy;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCausedBy() {
        return causedBy;
    }
}
