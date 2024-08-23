package interviewgether.authserver.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@NoArgsConstructor
public class OneTimePasscodeGenerator {

    private final int CODE_LENGTH = 8;

    public String generateCode(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        boolean hasDigits = false;
        boolean hasLetters = false;
        for(int i = 0; i < CODE_LENGTH; i++){
            boolean isDigit = secureRandom.nextBoolean();
            if(i >= CODE_LENGTH/2){
                if(!hasDigits) isDigit = true;
                if(!hasLetters) isDigit = false;
            }
            if(isDigit){
                hasDigits = true;
                code.append(secureRandom.nextInt(10));
            }
            else {
                hasLetters = true;
                code.append((char) (65 + secureRandom.nextInt(26)));
            }
        }
        return code.toString();
    }
}
