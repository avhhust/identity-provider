package interviewgether.authserver.exception.DAL;

import interviewgether.authserver.exception.ApiServiceException;

public class UserAlreadyExistsException extends ApiServiceException {

    public UserAlreadyExistsException(String message){
        this(message, "");
    }

    public UserAlreadyExistsException(String message, String causeFieldName) {
        super(message, causeFieldName);
    }
}
