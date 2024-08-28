package interviewgether.authserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OneTimePasscodeDTO {
    @Email
    private String email;

    @Pattern(regexp = "^[A-Z0-9]{8}$")
    private String code;

}
