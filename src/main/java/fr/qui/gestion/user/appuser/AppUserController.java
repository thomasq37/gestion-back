package fr.qui.gestion.user.appuser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.appart.AppartementService;
import fr.qui.gestion.appart.dto.AppartementForGestionDTO;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/utilisateurs", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class AppUserController {
	private final AppUserService appUserService;
	private final AppartementService appartementService;
	
	public AppUserController(AppUserService appUserService, AppartementService appartementService) {
		this.appUserService = appUserService;
		this.appartementService = appartementService;
	}
	@GetMapping("/{userId}/appartements")
	public ResponseEntity<Object> obtenirAppartementsParUserId(@PathVariable Long userId, HttpServletRequest request) {
	    String userToken = request.getHeader("X-API-USER-KEY");

	    Optional<AppUser> user = appUserService.findByUserToken(userToken);
	    if (!user.isPresent()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide.");
	    }
	    AppUser currentUser = user.get();
	    if (!currentUser.getId().equals(userId)) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Le token ne correspond pas à l'ID utilisateur fourni.");
	    }

	    // Si l'utilisateur est un gestionnaire
	    if ("GESTIONNAIRE".equals(currentUser.getRole().getName())) {
	        List<Appartement> appartementsGeres = appartementService.findByGestionnaireId(currentUser.getId());
	        List<AppartementForGestionDTO> dtos = appartementsGeres.stream()
	                                                              .map(appartementService::convertToDTO)
	                                                              .collect(Collectors.toList());
	        return ResponseEntity.ok(dtos);
	    } 
	    // Si l'utilisateur est un propriétaire
	    else if ("PROPRIETAIRE".equals(currentUser.getRole().getName())) {
	        List<Appartement> appartements = appUserService.obtenirAppartementsParUserId(userId);
	        return ResponseEntity.ok(appartements);
	    } 
	    // Si l'utilisateur n'a pas les droits nécessaires
	    else {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'avez pas les droits nécessaires pour accéder à ces informations.");
	    }
	}
	
	@GetMapping("/{userId}/appartements/{apartmentId}")
	public ResponseEntity<?> findAppartementByAppUserIdAndId(@PathVariable Long userId, @PathVariable Long apartmentId, HttpServletRequest request) {
	    String userToken = request.getHeader("X-API-USER-KEY");
	    Optional<AppUser> userOpt = appUserService.findByUserToken(userToken);
	    if (!userOpt.isPresent()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Token is invalid
	    }

	    AppUser currentUser = userOpt.get();
	    if (!currentUser.getId().equals(userId)) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Token and UserId don't match
	    }

	    Appartement appartement = appartementService.obtenirUnAppartementParId(apartmentId);
	    if (appartement == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appartement not found");
	    }

	    if ("PROPRIETAIRE".equals(currentUser.getRole().getName())) {
	        if (!appartement.getAppUser().getId().equals(currentUser.getId())) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Apartment doesn't belong to user
	        }
	        return ResponseEntity.ok(appartementService.convertToPrioprioDTO(appartement));
	    } else if ("GESTIONNAIRE".equals(currentUser.getRole().getName())) {
	        if (appartementService.estGestionnaireDeAppartement(currentUser, appartement)) {
	            AppartementForGestionDTO dto = appartementService.convertToDTO(appartement);
	            return ResponseEntity.ok(dto);
	        }
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not the manager of this apartment");
	    } else {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have the right to access this information.");
	    }
	}

}
