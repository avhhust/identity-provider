package interviewgether.authserver.exception.DAL;

public class EmailAlreadyExistsException extends UserAlreadyExistsException {
    private static final String causedBy = "email";
    public EmailAlreadyExistsException(){
        super(causedBy);
    }
    public EmailAlreadyExistsException(String message) {
        super(message, causedBy);
    }
}
