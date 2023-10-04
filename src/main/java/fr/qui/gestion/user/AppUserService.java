package fr.qui.gestion.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;

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
	public List<Appartement> obtenirAppartementsParUserToken(String userToken) {
		Optional<AppUser> optionalUser = userRepository.findByUserToken(userToken);
		if(optionalUser.isPresent()) {
			return optionalUser.get().getAppartements();
		}
		else {
            throw new IllegalArgumentException("Appartements not found for the provided user.");
		}
	}

}
