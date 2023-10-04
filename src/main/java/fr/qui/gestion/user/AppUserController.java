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
	
	@GetMapping("/{userToken}/appartements")
	public List<Appartement> obtenirAppartementsParUserToken(@PathVariable String userToken){
		return appUserService.obtenirAppartementsParUserToken(userToken);
		
	}
}
