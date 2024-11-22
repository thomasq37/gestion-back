package fr.qui.gestion.v2.auth;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
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
    public SuccessResponse registerUser(@RequestBody Utilisateur utilisateurRequest, HttpServletRequest request) {
        return authService.registerUser(utilisateurRequest, request);
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> authenticateUser(@RequestBody Utilisateur loginRequest, HttpServletRequest request) {
        return authService.authenticateUser(loginRequest, request);
    }
}
