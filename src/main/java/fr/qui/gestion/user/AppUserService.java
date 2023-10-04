package fr.qui.gestion.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
	
	@Autowired
	private UserRepository userRepository;
	    

	public Optional<AppUser> findByUserToken(String userToken) {
		return userRepository.findByUserToken(userToken);
	}
	public Long getUserIdByUserToken(String userToken) {
		Optional<AppUser> user = userRepository.findByUserToken(userToken);
        if (user.isPresent()) {
            return user.get().getId();
        } else {
            throw new IllegalArgumentException("User not found for the provided token.");
        }
    }

}
