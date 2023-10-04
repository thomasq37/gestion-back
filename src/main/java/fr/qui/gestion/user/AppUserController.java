package fr.qui.gestion.user;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.qui.gestion.appart.Appartement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/utilisateurs", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class AppUserController {
	private final AppUserService appUserService;
	
	public AppUserController(AppUserService appUserService) {
		this.appUserService = appUserService;
	}
	
	@GetMapping("/{userId}/appartements")
	public List<Appartement> obtenirAppartementsParUserId(@PathVariable long userId){
		return appUserService.obtenirAppartementsParUserId(userId);
		
	}
	
	@GetMapping("/{userId}/appartements/{apartmentId}")
    public ResponseEntity<Appartement> findAppartementByAppUserIdAndId(@PathVariable Long userId, @PathVariable Long apartmentId, HttpServletRequest request) {
		try {
	        String userToken = request.getHeader("X-API-USER-KEY");
	
	        Optional<AppUser> user = appUserService.findByUserToken(userToken);
	        if(!user.isPresent()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Token is invalid
	        }
	        
			Appartement appartement = appUserService.findAppartementByAppUserIdAndId(userId, apartmentId);
	        if(!appartement.getAppUser().getId().equals(user.get().getId())) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Apartment doesn't belong to user
	        }
	        return ResponseEntity.ok(appartement);
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
    }
}
