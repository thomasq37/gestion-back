package fr.qui.gestion.utilisateur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "${app.cors.origin}")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/profil")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<?> obtenirUtilisateur() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); 
        try {
			Utilisateur utilisateur = utilisateurService.obtenirUtilisateur(email);
	        return ResponseEntity.ok().body(utilisateur);

		} catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason()); 
		}
    }
    
    @PatchMapping("/modifier")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<?> modifierUtilisateur(@RequestBody Utilisateur utilisateurModifie) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        try {
			utilisateurService.modifierUtilisateur(email, utilisateurModifie);
	        return ResponseEntity.ok().body("Le compte a été mis à jour avec succès.");

		} catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason()); 
		}

    }

    @DeleteMapping("/supprimer")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<?> supprimerCompte() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        try {
            utilisateurService.supprimerCompte(email);
            return ResponseEntity.ok().body("Le compte a été supprimé avec succès.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @DeleteMapping("/admin/supprimer/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> supprimerUtilisateur(@PathVariable String email) {
        try {
            utilisateurService.supprimerUtilisateurParAdmin(email);
            return ResponseEntity.ok().body("Le compte : " + email + " a été supprimé avec succès.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
