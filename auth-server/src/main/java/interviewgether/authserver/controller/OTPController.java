package interviewgether.authserver.controller;

import interviewgether.authserver.dto.OneTimePasscodeDTO;
import interviewgether.authserver.model.AuthUser;
import interviewgether.authserver.model.OneTimePasscode;
import interviewgether.authserver.notifications.EmailInstance;
import interviewgether.authserver.service.OneTimePasscodeService;
import interviewgether.authserver.service.UserService;
import interviewgether.authserver.service.impl.EmailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/otp")
@AllArgsConstructor
public class OTPController {

    private final OneTimePasscodeService oneTimePasscodeService;
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping()
    public ResponseEntity<?> generateOtpAndSendToEmail(@RequestBody Map<String, @Email String> body){
        String email = body.get("email");
        AuthUser user = userService.readByEmail(body.get("email"));
        OneTimePasscode otp = oneTimePasscodeService.create(user);
        EmailInstance message = new EmailInstance(
                email,
                "One Time Passcode",
                "Code: " + otp.getCode() + ". It's only valid for 5 minutes"
        );
        emailService.sendSimpleMessage(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<?> verifyOtpCode(@RequestBody @Valid OneTimePasscodeDTO oneTimePasscodeDTO){
        oneTimePasscodeService.verifyOtpCode(oneTimePasscodeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}