package fr.qui.gestion.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.qui.gestion.role.Role;
import fr.qui.gestion.role.RoleRepository;
import fr.qui.gestion.security.CustomUtilisateurDetailsService;
import fr.qui.gestion.security.jwt.JwtUtils;
import fr.qui.gestion.utilisateur.Utilisateur;
import fr.qui.gestion.utilisateur.UtilisateurRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth", produces = { "application/json" })
@CrossOrigin(origins = "${app.cors.origin}")
public class AuthController {
	
    /*@Autowired
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
    public ResponseEntity<Object> createGestionnaire(@PathVariable Long userId, @PathVariable Long appartId, @RequestBody UserRequest userRequest) {
    	
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
            	AppUserDTO userDto = authenticationService.convertToDTO(createdUser);
                return ResponseEntity.ok(userDto);
            }
            else {
            	return ResponseEntity.status(400).body(Collections.singletonMap("message", "Token d'invitation invalide"));
            }
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AppUser credentials) {
        try {
            Map<String, Object> response = authenticationService.authenticate(credentials.getUsername(), credentials.getPassword());
            System.out.println(response);
            if(response.get("error") == null) {
            	String userToken = (String) response.get("userToken");
                Long userId = (Long) response.get("userId");
                String userRole = (String) response.get("userRole");
                Map<String, Object> credForFront = new HashMap<>();
                credForFront.put("userId", userId);
                credForFront.put("userToken", userToken);
                credForFront.put("userRole", userRole);
                credForFront.put("token", token);
                return ResponseEntity.ok(credForFront);
            }
            else {
                return ResponseEntity.status(401).body(response.get("error"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    */

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomUtilisateurDetailsService customUtilisateurDetailsService;
    private final TentativeBlocageService tentativeBlocageService;

    public AuthController(UtilisateurRepository utilisateurRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          CustomUtilisateurDetailsService customUtilisateurDetailsService,
                          TentativeBlocageService tentativeBlocageService) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.customUtilisateurDetailsService = customUtilisateurDetailsService;
        this.tentativeBlocageService = tentativeBlocageService;
    }

    @PostMapping("/inscription")
    public ResponseEntity<String> registerUser(@RequestBody Utilisateur utilisateurRequest, HttpServletRequest request) {
        try {
            String ip = request.getRemoteAddr();
            String mdp = utilisateurRequest.getMdp();
            if (!tentativeBlocageService.peutTenterInscription(ip)) {
                return ResponseEntity.badRequest().body("Trop de tentatives d'inscription depuis cette adresse IP, veuillez réessayer plus tard.");
            }
            if (mdp.length() < 8) {
                return ResponseEntity.badRequest().body("Erreur: Le mot de passe doit contenir au moins 8 caractères.");
            }

            // Vérification de la présence d'au moins un chiffre
            if (!mdp.matches(".*\\d.*")) {
                return ResponseEntity.badRequest().body("Erreur: Le mot de passe doit contenir au moins un chiffre.");
            }

            // Vérification de la présence d'au moins un caractère spécial
            if (!mdp.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                return ResponseEntity.badRequest().body("Erreur: Le mot de passe doit contenir au moins un caractère spécial.");
            }
            // Vérifier si le nom est déjà pris
            if (utilisateurRepository.existsByEmail(utilisateurRequest.getEmail())) {
                return ResponseEntity.badRequest().body("Erreur: Ce mail est déjà pris!");
            }
            if (utilisateurRepository.existsByPseudo(utilisateurRequest.getPseudo())) {
                return ResponseEntity.badRequest().body("Erreur: Ce pseudo est déjà pris!");
            }

            // Création et enregistrement de l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail(utilisateurRequest.getEmail());
            utilisateur.setPseudo(utilisateurRequest.getPseudo());

            utilisateur.setMdp(passwordEncoder.encode(utilisateurRequest.getMdp()));

            // Déterminer le rôle en fonction du nombre d'utilisateurs déjà inscrits
            Role role;
            long nbUtilisateurs = utilisateurRepository.count();
            if (nbUtilisateurs == 0) {
                // Si c'est le premier utilisateur, lui donner le rôle ROLE_ADMIN
                role = roleRepository.findByNom("ROLE_ADMIN")
                        .orElseThrow(() -> new Exception("Rôle administrateur non trouvé"));
            } else {
                // Sinon, lui donner le rôle ROLE_USER par défaut
                role = roleRepository.findByNom("ROLE_USER")
                        .orElseThrow(() -> new Exception("Rôle utilisateur non trouvé"));
            }

            // Attribuer le rôle à l'utilisateur
            utilisateur.setRoles(Collections.singleton(role));

            Utilisateur utilisateurEnregistre = utilisateurRepository.save(utilisateur);
            // Enregistrer l'utilisateur avec les rôles

            return ResponseEntity.ok("Utilisateur enregistré avec succès!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @PostMapping("/connexion")
    public ResponseEntity<String> authenticateUser(@RequestBody Utilisateur loginRequest, HttpServletRequest request) {

        try {
            String ip = request.getRemoteAddr();
            if (!tentativeBlocageService.peutTenterConnexion(ip)) {
                return ResponseEntity.badRequest().body("Trop de tentatives de connexion depuis cette adresse IP, veuillez réessayer plus tard.");
            }
            UserDetails userDetails = customUtilisateurDetailsService.loadUserByUsername(loginRequest.getEmail());
            if (passwordEncoder.matches(loginRequest.getMdp(), userDetails.getPassword())) {
                List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .toList();
                String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(), roles);
                return ResponseEntity.ok(jwt);
            } else {
                return ResponseEntity.badRequest().body("Erreur: Email ou mot de passe incorrect!");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("Erreur: Email ou mot de passe incorrect!");
        }
    }
    
}
