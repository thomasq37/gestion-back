package fr.qui.gestion.auth;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import fr.qui.gestion.utilisateur.Utilisateur;
import fr.qui.gestion.utilisateur.UtilisateurDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	
	/*
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleARepository roleARepository;
    
    @Autowired
    private InvitationRepository invitationRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AppUser createUser(String username, String password, String role) {
    	// Check if the role already exists
        Optional<RoleA> optionalRole = roleARepository.findByName(role);
        RoleA defaultRoleA;
        if(optionalRole.isPresent()) {
        	defaultRoleA = optionalRole.get();
        }
        else {
        	defaultRoleA = new RoleA();
        	defaultRoleA.setName(role);
            roleARepository.save(defaultRoleA);
        }
        
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserToken(generateRandomToken(24));
        user.setRoleA(defaultRoleA);
        return userRepository.save(user);
    }
    public String generateRandomToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[byteLength];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public Map<String, Object> authenticate(String username, String password) {
        Optional<AppUser> optionalUser = userRepository.findByUsername(username);
        Map<String, Object> response = new HashMap<>();
        if (optionalUser.isPresent()) {
            AppUser user = optionalUser.get();
            
            // Vérifiez que le mot de passe correspond à celui stocké dans la base de données
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Si les mots de passe correspondent, ajoutez les informations de l'utilisateur à la réponse
                response.put("userId", user.getId());
                response.put("userToken", user.getUserToken());
                response.put("userRole", user.getRoleA().getName());
            } else {
                // Si les mots de passe ne correspondent pas, indiquez une erreur d'authentification
                response.put("error", "Nom d'utilisateur ou mot de passe incorrect");
            }
        } else {
            // Si l'utilisateur n'existe pas, indiquez également une erreur d'authentification
            response.put("error", "Nom d'utilisateur ou mot de passe incorrect");
        }
        return response;
    }
*/
  /*
    public boolean validateInvitation(String token) {
        Invitation invitation = invitationRepository.findByToken(token);
        if(invitation != null && !invitation.isUsed()) {
        	invitation.setUsed(true);
            invitationRepository.save(invitation);
            return true;
        }
        return false;
    }
    
	public UtilisateurDTO convertToDTO(Utilisateur user) {
        UtilisateurDTO dto = new UtilisateurDTO();
		dto.setId(user.getId());
		dto.setPseudo(user.getPseudo());
		dto.setPhoneNumber(user.getPhoneNumber());
	    dto.setEmail(user.getEmail());
	    return dto;
	}*/
}