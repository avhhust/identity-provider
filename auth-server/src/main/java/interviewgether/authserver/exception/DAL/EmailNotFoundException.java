package interviewgether.authserver.exception.DAL;

public class EmailNotFoundException extends UserNotFoundException{
    private final static String causedBy = "email";

    public EmailNotFoundException(){
        this("Email doesn't exist");
    }
    public EmailNotFoundException(String message){
        super(message, causedBy);
    }
}
