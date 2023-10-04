package fr.qui.gestion.user;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.qui.gestion.appart.Appartement;

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
}
