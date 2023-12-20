package fr.qui.gestion.auth;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.appart.dto.AppartementForGestionDTO;
import fr.qui.gestion.user.UserRepository;
import fr.qui.gestion.user.appuser.AppUser;
import fr.qui.gestion.user.appuser.AppUserDTO;
import fr.qui.gestion.user.role.Role;
import fr.qui.gestion.user.role.RoleRepository;

@Service
public class AuthService {
	
	  
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private InvitationRepository invitationRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AppUser createUser(String username, String password, String role) {
    	// Check if the role already exists
        Optional<Role> optionalRole = roleRepository.findByName(role);
        Role defaultRole;
        if(optionalRole.isPresent()) {
        	defaultRole = optionalRole.get();
        }
        else {
        	defaultRole = new Role();
        	defaultRole.setName(role);
            roleRepository.save(defaultRole);
        }
        
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserToken(generateRandomToken(24));
        user.setRole(defaultRole);
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
                response.put("userRole", user.getRole().getName());
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

    
    public boolean validateInvitation(String token) {
        Invitation invitation = invitationRepository.findByToken(token);
        if(invitation != null && !invitation.isUsed()) {
        	invitation.setUsed(true);
            invitationRepository.save(invitation);
            return true;
        }
        return false;
    }
    
	public AppUserDTO convertToDTO(AppUser user) {
		AppUserDTO dto = new AppUserDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setPhoneNumber(user.getPhoneNumber());
	    dto.setEmail(user.getEmail());
	    return dto;
	}
}