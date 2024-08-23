package interviewgether.authserver.service;


import interviewgether.authserver.dto.OneTimePasscodeDTO;
import interviewgether.authserver.model.AuthUser;
import interviewgether.authserver.model.OneTimePasscode;

public interface OneTimePasscodeService {
    OneTimePasscode create(OneTimePasscode oneTimePasscode);
    OneTimePasscode create(AuthUser user); // ToDo: REVISE

    void delete(String otpCode);
    OneTimePasscode readByCode(String code);
    void verifyOtpCode(OneTimePasscodeDTO codeDto);
}
