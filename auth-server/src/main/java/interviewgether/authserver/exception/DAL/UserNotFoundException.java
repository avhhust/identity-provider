package interviewgether.authserver.exception.DAL;

import interviewgether.authserver.exception.ApiServiceException;

public class UserNotFoundException extends ApiServiceException {

    public UserNotFoundException(String message, String causedBy) {
        super(message, causedBy);
    }
}
