package fr.qui.gestion.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.qui.gestion.user.User;
import fr.qui.gestion.user.UserRepository;

@Service
public class AuthService {
	
	  
    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(String username, String password) {
    	System.out.println("auth_service");

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }


    public boolean authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}