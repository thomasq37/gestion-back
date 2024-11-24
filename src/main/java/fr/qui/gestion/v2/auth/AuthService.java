package fr.qui.gestion.v2.auth;

import fr.qui.gestion.v2.entites.Role.Role;
import fr.qui.gestion.v2.entites.Role.RoleRepository;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.ErrorResponse;
import fr.qui.gestion.v2.exception.SuccessResponse;
import fr.qui.gestion.v2.exception.TooManyAttemptsException;
import fr.qui.gestion.v2.security.CustomUtilisateurDetailsService;
import fr.qui.gestion.v2.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomUtilisateurDetailsService customUtilisateurDetailsService;
    private final TentativeBlocageService tentativeBlocageService;

    public AuthService(UtilisateurRepository utilisateurRepository,
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

    public SuccessResponse registerUser(RegisterUserRequestDTO registerUserRequestDTO, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String mdp = registerUserRequestDTO.getMdp();
        String telephone = registerUserRequestDTO.getTelephone();
        String email = registerUserRequestDTO.getEmail();

        if (!tentativeBlocageService.peutTenterInscription(ip)) {
            throw new TooManyAttemptsException("Trop de tentatives de connexion depuis cette adresse IP.");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire.");
        }
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailPattern)) {
            throw new IllegalArgumentException("L'email n'est pas valide.");
        }
        if (mdp == null || mdp.length() < 8 || !mdp.matches(".*\\d.*") || !mdp.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new IllegalArgumentException("Le mot de passe doit respecter les critères suivants : au moins 8 caractères, un chiffre, et un caractère spécial.");
        }
        if (telephone != null && !telephone.matches("^(\\+\\d{1,3}[- ]?)?\\d{10}$")) {
            throw new IllegalArgumentException("Numéro de téléphone invalide.");
        }

        if (utilisateurRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ce mail est déjà pris!");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setNom(registerUserRequestDTO.getNom());
        utilisateur.setPrenom(registerUserRequestDTO.getPrenom());
        utilisateur.setTelephone(telephone);
        utilisateur.setMdp(passwordEncoder.encode(mdp));

        Role role = utilisateurRepository.count() == 0
                ? roleRepository.findByNom("ROLE_ADMIN").orElseThrow(() -> new SecurityException("Rôle administrateur non trouvé"))
                : roleRepository.findByNom("ROLE_USER").orElseThrow(() -> new SecurityException("Rôle utilisateur non trouvé"));

        utilisateur.setRoles(Collections.singleton(role));
        utilisateurRepository.save(utilisateur);

        return new SuccessResponse("Utilisateur enregistré avec succès!");
    }

    public ResponseEntity<?> authenticateUser(AuthenticateUserRequestDTO loginRequest, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (!tentativeBlocageService.peutTenterConnexion(ip)) {
            throw new TooManyAttemptsException("Trop de tentatives de connexion depuis cette adresse IP.");
        }
        try {
            UserDetails userDetails = customUtilisateurDetailsService.loadUserByUsername(loginRequest.getEmail());
            if (passwordEncoder.matches(loginRequest.getMdp(), userDetails.getPassword())) {
                List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
                String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(), roles);
                return ResponseEntity.ok(jwt);
            }
        }
        catch (UsernameNotFoundException ex){
            return ResponseEntity.badRequest().body(new ErrorResponse("Erreur: Email ou mot de passe incorrect!"));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Erreur: Email ou mot de passe incorrect!"));
    }
}
