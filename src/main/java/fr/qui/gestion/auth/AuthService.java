package fr.qui.gestion.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.qui.gestion.user.AppUser;
import fr.qui.gestion.user.UserRepository;

@Service
public class AuthService {
	
	  
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private InvitationRepository invitationRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AppUser createUser(String username, String password) {
    	System.out.println("auth_service");

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }


    public Map<String, Object> authenticate(String username, String password) {
        Optional<AppUser> optionalUser = userRepository.findByUsername(username);
        Map<String, Object> response = new HashMap<>();
        if(optionalUser.isPresent()) {
            AppUser user = optionalUser.get();
            response.put("appUserId", user.getId());
            response.put("isAuthentificated", passwordEncoder.matches(password, user.getPassword()));
        }
        else {
        	  response.put("appUserId", 0);
              response.put("isAuthentificated", false);
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