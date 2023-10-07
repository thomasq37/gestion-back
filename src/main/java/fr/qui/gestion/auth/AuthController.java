package fr.qui.gestion.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.qui.gestion.user.AppUser;
import fr.qui.gestion.user.UserRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.cors.origin}")
public class AuthController {
	
    @Autowired
    private AuthService authenticationService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Value("${api.key}")
    private String token;
    
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
    	
        try {
            AppUser user = userRequest.getUser();
            String token = userRequest.getToken();
            if(authenticationService.validateInvitation(token)) {
            	 AppUser createdUser = authenticationService.createUser(user.getUsername(), user.getPassword());
                 return ResponseEntity.ok("Compte créé avec succès : id = " + createdUser.getUsername());
            }
            else {
            	return ResponseEntity.status(400).body("Invalide token invitation");
            }
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AppUser credentials) {
        try {
            Map<String, Object> response = authenticationService.authenticate(credentials.getUsername(), credentials.getPassword());
            
            String userToken = (String) response.get("userToken");
            Long userId = (Long) response.get("userId");
            String userRole = (String) response.get("userRole");
            if(userId != -1 && userToken != "") {
                Map<String, Object> credForFront = new HashMap<>();
                credForFront.put("userId", userId);
                credForFront.put("userToken", userToken);
                credForFront.put("userRole", userRole);
                credForFront.put("token", token);
                return ResponseEntity.ok(credForFront);
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    
    
}
