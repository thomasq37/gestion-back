package fr.qui.gestion.user.appuser;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.appart.dto.AppartementForGestionDTO;
import fr.qui.gestion.user.UserRepository;

@Service
public class AppUserService {
	
	@Autowired
	private UserRepository userRepository;
	    

	public Optional<AppUser> findByUserToken(String userToken) {
		return userRepository.findByUserToken(userToken);
	}

	public List<Appartement> obtenirAppartementsParUserId(Long userId) {
		Optional<AppUser> optionalUser = userRepository.findById(userId);
		if(optionalUser.isPresent()) {
			return optionalUser.get().getAppartements();
		}
		else {
            throw new IllegalArgumentException("Appartements not found for the provided user.");
		}
	}

	public Appartement findAppartementByAppUserIdAndId(Long userId, Long apartmentId) {
        return userRepository.findAppartementByAppUserIdAndId(userId, apartmentId).orElse(null);
    }
	
}
