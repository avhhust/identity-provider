package interviewgether.authserver.controller;

import interviewgether.authserver.dto.UserRegisterDTO;
import interviewgether.authserver.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody @Valid UserRegisterDTO userRegisterDTO){
        userService.create(userRegisterDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
