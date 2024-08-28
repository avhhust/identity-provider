package interviewgether.authserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDTO {
    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "One-time passcode is required")
    @Pattern(regexp = "^[A-Z0-9]{8}$")
    private String code;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+")
    private String password;

}
