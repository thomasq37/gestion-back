package fr.qui.gestion.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.qui.gestion.user.User;
import fr.qui.gestion.user.UserRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.cors.origin}")
public class AuthController {
	
    @Autowired
    private AuthService authenticationService;
    

    @Value("${api.key}")
    private String token;
    
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
    	
        try {
            User user = userRequest.getUser();
            String token = userRequest.getToken();
            if(authenticationService.validateInvitation(token)) {
            	 User createdUser = authenticationService.createUser(user.getUsername(), user.getPassword());
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
    public ResponseEntity<String> login(@RequestBody User credentials) {
	  try {
	      if(authenticationService.authenticate(credentials.getUsername(), credentials.getPassword())) {
	          return ResponseEntity.ok(token);
	      } else {
	          return ResponseEntity.status(401).body("Invalid credentials");
	      }
       } catch (Exception e) {
          return ResponseEntity.status(500).body(e.getMessage());
       }
    }
    
    
}
