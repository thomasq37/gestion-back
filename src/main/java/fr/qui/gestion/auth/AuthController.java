package fr.qui.gestion.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.qui.gestion.invit.InvitationService;
import fr.qui.gestion.user.User;
import fr.qui.gestion.user.UserRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
    @Autowired
    private AuthService authenticationService;
    

    @Autowired
    private InvitationService invitationService; 

    @Value("${api.key}")
    private String token;

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
    @PostMapping("/createUser")
    public ResponseEntity<Object> createUser(@RequestBody UserRequest userRequest) {
        try {
            User user = userRequest.getUser();
            String token = userRequest.getToken();
            if(invitationService.validateInvitation(token)) {
            	 User createdUser = authenticationService.createUser(user.getUsername(), user.getPassword());
                 return ResponseEntity.ok(createdUser);            }
            else {
            	return ResponseEntity.status(400).body("Invalide token invitation");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
