package interviewgether.authserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class UserLoginDTO {
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?!^\\d+$)[a-zA-Z\\d]+$", message = "Username can only contain letters and numbers")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters long")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+", message = "Password should include at least one capital letter and one number.")
    private String password;

    public UserLoginDTO() {}

    public UserLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLoginDTO that = (UserLoginDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
