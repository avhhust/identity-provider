package interviewgether.authserver.service.impl;

import interviewgether.authserver.dto.OneTimePasscodeDTO;
import interviewgether.authserver.exception.DAL.OneTimePasscodeNotValidException;
import interviewgether.authserver.model.AuthUser;
import interviewgether.authserver.model.OneTimePasscode;
import interviewgether.authserver.repository.OneTimePasscodeRepository;
import interviewgether.authserver.service.OneTimePasscodeService;
import interviewgether.authserver.util.OneTimePasscodeGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OneTimePasscodeServiceImpl implements OneTimePasscodeService {

    private final OneTimePasscodeGenerator oneTimePasscodeGenerator;
    private final OneTimePasscodeRepository oneTimePasscodeRepository;

    @Override
    @Transactional
    public OneTimePasscode create(OneTimePasscode otp) {
        Assert.notNull(otp, "OneTimePasscode cannot be null");
        oneTimePasscodeRepository.findByUser(otp.getUser())
                                 .ifPresent((prevOtp) -> oneTimePasscodeRepository.deleteByCode(prevOtp.getCode()));

        return oneTimePasscodeRepository.save(otp);
    }

    @Override
    @Transactional
    public OneTimePasscode create(AuthUser user) {
        Assert.notNull(user, "User cannot be null");
        LocalDateTime currTime = LocalDateTime.now();
        LocalDateTime expTime = currTime.plusMinutes(5);
        OneTimePasscode oneTimePasscode = new OneTimePasscode(
            oneTimePasscodeGenerator.generateCode(),
            currTime,
            expTime,
            user
        );
        return create(oneTimePasscode);
    }

    @Override
    public OneTimePasscode readByCode(String code) {
        Assert.notNull(code, "Code cannot be null");
        return oneTimePasscodeRepository
                .findByCode(code)
                .orElseThrow(OneTimePasscodeNotValidException::new);
    }

    @Override
    public void delete(String code) {
        OneTimePasscode otp = readByCode(code);
        oneTimePasscodeRepository.delete(otp);
    }

    @Override
    @Transactional
    public void verifyOtpCode(OneTimePasscodeDTO otpCodeDto) {
        OneTimePasscode otp = readByCode(otpCodeDto.getCode());
        AuthUser user = otp.getUser();

        //check if otp associated with user
        if(user == null){
            throw new OneTimePasscodeNotValidException();
        }
        // check if codeDTO email matches user's email associated with OTP
        if(!otpCodeDto.getEmail().equals(user.getEmail())){
            throw new OneTimePasscodeNotValidException();
        }
        LocalDateTime currTime = LocalDateTime.now();
        if(otp.getExpiresAt().isBefore(currTime)){
            throw new OneTimePasscodeNotValidException("One-time passcode has expired");
        }
        otp.setConfirmedAt(currTime);
    }
}