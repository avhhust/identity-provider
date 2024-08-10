package interviewgether.authserver.exception.DAL;

public class UsernameAlreadyExistsException extends UserAlreadyExistsException {
    private final static String causedBy = "username";
    public UsernameAlreadyExistsException() {
        super(causedBy);
    }
    public UsernameAlreadyExistsException(String message) {
        super(message, causedBy);
    }
}
