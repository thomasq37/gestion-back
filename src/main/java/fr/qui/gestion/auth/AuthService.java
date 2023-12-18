package fr.qui.gestion.auth;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.qui.gestion.user.AppUser;
import fr.qui.gestion.user.Role;
import fr.qui.gestion.user.RoleRepository;
import fr.qui.gestion.user.UserRepository;

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
        if(optionalUser.isPresent()) {
	        AppUser user = optionalUser.get();
	        response.put("userId", user.getId());
	        response.put("userToken", user.getUserToken());
	        response.put("userRole", user.getRole().getName());
        }
        else {
      	  response.put("userId", -1);	
    	  response.put("userToken", "");
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
}