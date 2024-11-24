package fr.qui.gestion.v2.auth;
import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = "${app.cors.origin}")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/inscription")
    public SuccessResponse registerUser(@RequestBody RegisterUserRequestDTO registerUserRequestDTO, HttpServletRequest request) {
        return authService.registerUser(registerUserRequestDTO, request);
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticateUserRequestDTO authenticateUserRequestDTO, HttpServletRequest request) {
        return authService.authenticateUser(authenticateUserRequestDTO, request);
    }
}
