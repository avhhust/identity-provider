package interviewgether.authserver.exception.DAL;

import interviewgether.authserver.exception.ApiServiceException;

public class UsernameNotFoundException extends ApiServiceException {
    private final static String causedBy = "username";

    public UsernameNotFoundException(){
        this("Username doesn't exist");
    }
    public UsernameNotFoundException(String message) {
        super(message, causedBy);
    }
}
