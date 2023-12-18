package fr.qui.gestion.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.appart.AppartementService;
import fr.qui.gestion.user.AppUser;
import fr.qui.gestion.user.UserRequest;

@RestController
@RequestMapping(value = "/api/auth", produces = { "application/json" })
@CrossOrigin(origins = "${app.cors.origin}")
public class AuthController {
	
    @Autowired
    private AuthService authenticationService;
    
    @Autowired
    private AppartementService appartementService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Value("${api.key}")
    private String token;
    
    @PostMapping("/create")
    public ResponseEntity<Map<String,String>> createUser(@RequestBody UserRequest userRequest) {
    	
        try {
            AppUser user = userRequest.getUser();
            String token = userRequest.getToken();
            if(authenticationService.validateInvitation(token)) {
            	 AppUser createdUser = authenticationService.createUser(user.getUsername(), user.getPassword(), "PROPRIETAIRE");
            	 return ResponseEntity.ok(Collections.singletonMap("message", "Compte créé avec succès : Nom d'utilisateur = " + createdUser.getUsername()));
            }
            else {
            	return ResponseEntity.status(400).body(Collections.singletonMap("message", "Token d'invitation invalide"));
            }
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(Collections.singletonMap("message", e.getMessage()));
        }
    }
    
    @PostMapping("utilisateurs/{userId}/appartements/{appartId}/gestionnaire/ajouter")
    public ResponseEntity<String> createGestionnaire(@PathVariable Long userId, @PathVariable Long appartId, @RequestBody UserRequest userRequest) {
    	
        try {
            AppUser user = userRequest.getUser();
            String token = userRequest.getToken();
            if(authenticationService.validateInvitation(token)) {
            	Appartement currentAppart = appartementService.obtenirUnAppartementParId(appartId);
            	AppUser createdUser = authenticationService.createUser(user.getUsername(), user.getPassword(), "GESTIONNAIRE");
            	List<AppUser> appUserList = currentAppart.getGestionnaires();
            	appUserList.add(createdUser);
            	currentAppart.setGestionnaires(appUserList);
            	appartementService.mettreAJourUnAppartementPourUtilisateur(userId, appartId, currentAppart);
                return ResponseEntity.ok("Compte créé avec succès : id = " + createdUser.getId());
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
