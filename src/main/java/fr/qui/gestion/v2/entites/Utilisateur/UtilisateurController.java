package fr.qui.gestion.v2.entites.Utilisateur;

import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<UtilisateurDTO> obtenirUtilisateur() {
        return ResponseEntity.ok(utilisateurService.obtenirUtilisateur());
    }

    @PatchMapping("/modifier")
    public ResponseEntity<UtilisateurDTO> modifierUtilisateur(@RequestBody Utilisateur utilisateurModifie) {
        return ResponseEntity.ok(utilisateurService.modifierUtilisateur(utilisateurModifie));
    }

    @DeleteMapping("/supprimer")
    public ResponseEntity<SuccessResponse> supprimerCompte() {
        return ResponseEntity.ok(utilisateurService.supprimerCompte());
    }

    @DeleteMapping("/admin/supprimer/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SuccessResponse> supprimerUtilisateur(@PathVariable String email) {
        return ResponseEntity.ok(utilisateurService.supprimerUtilisateur(email));
    }
}