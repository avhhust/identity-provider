package interviewgether.authserver.exception.DAL;

import interviewgether.authserver.exception.ApiServiceException;

public class OneTimePasscodeNotValidException extends ApiServiceException {

    private final static String causedBy = "code";

    public OneTimePasscodeNotValidException() {
        super("One-time passcode is not valid", causedBy);
    }

    public OneTimePasscodeNotValidException(String message){
        super(message, causedBy);
    }

}