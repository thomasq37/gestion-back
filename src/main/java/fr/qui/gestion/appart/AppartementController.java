package fr.qui.gestion.appart;

import java.util.List;
import java.util.Optional;

import fr.qui.gestion.appart.dto.AdresseDTO;
import fr.qui.gestion.appart.dto.AppartementCCDTO;
import fr.qui.gestion.utilisateur.Utilisateur;
import fr.qui.gestion.utilisateur.UtilisateurDTO;
import fr.qui.gestion.utilisateur.UtilisateurService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.qui.gestion.frais.Frais;

@RestController
@RequestMapping(path = "/api/appartements", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class AppartementController {

    private final AppartementService appartementService;
    @Autowired
    public AppartementController(AppartementService appartementService) {
        this.appartementService = appartementService;
    }

    // Appartements



    @DeleteMapping("/{appartId}")
    @Transactional
    public ResponseEntity<String> supprimerUnAppartement(@PathVariable Long appartId) {
        appartementService.supprimerTousLesFraisParAppartementId(appartId);
        appartementService.supprimerUnAppartement(appartId);
        return new ResponseEntity<>("Appartement deleted successfully", HttpStatus.OK);
    }
    // Periode
    // Frais Periodes

    @PostMapping("/periodes/{periodeId}/frais")
    public ResponseEntity<Frais> ajouterUnFraisPourPeriode(
            @PathVariable Long periodeId,
            @RequestBody Frais newFrais) {
        try {
            Frais frais = appartementService.ajouterUnFraisPourPeriode(periodeId, newFrais);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/periodes/{periodeId}/frais/{fraisId}")
    public ResponseEntity<Frais> mettreAJourUnFraisPourPeriode(@PathVariable Long periodeId, @PathVariable Long fraisId, @RequestBody Frais fraisMisAJour) {
        try {
            Frais frais = appartementService.mettreAJourUnFraisPourPeriode(periodeId, fraisId, fraisMisAJour);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/periodes/{periodeId}/frais/{fraisId}")
    @Transactional
    public ResponseEntity<?> supprimerFraisPourPeriode(@PathVariable Long periodeId, @PathVariable Long fraisId) {
        try {
            appartementService.supprimerFraisPourPeriode(periodeId, fraisId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/{appartementId}/gestionnaires/{gestionnaireId}")
    public ResponseEntity<UtilisateurDTO> mettreAJourUnGestionnairePourAppartement(
            @PathVariable Long userId,
            @PathVariable Long appartementId,
            @PathVariable(required = false) Long gestionnaireId,
            @RequestBody UtilisateurDTO modifieGestionnaire) {
        try {
            UtilisateurDTO gestionnaire = appartementService.mettreAJourUnGestionnairePourAppartement(userId, appartementId, gestionnaireId, modifieGestionnaire);
            return ResponseEntity.ok(gestionnaire);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{appartementId}/gestionnaires/{gestionnaireId}")
    public ResponseEntity<?> supprimerUnGestionnairePourAppartement(
            @PathVariable Long userId,
            @PathVariable Long appartementId,
            @PathVariable Long gestionnaireId) {
        try {
            appartementService.supprimerUnGestionnairePourAppartement(userId, appartementId, gestionnaireId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // utilisé v2 //
    @GetMapping("/adresses")
    public ResponseEntity<Object> obtenirAdressesAppartementsParUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Si l'utilisateur est un gestionnaire
        if (auth.getAuthorities().stream().anyMatch(authority -> "ROLE_GESTIONNAIRE".equals(authority.getAuthority()))) {
            List<AdresseDTO> adressesAppartements = appartementService.obtenirAdressesAppartementsParGestionnaireId(auth.getName());
            return ResponseEntity.ok(adressesAppartements);
        }
        // Si l'utilisateur est un propriétaire
        else if (auth.getAuthorities().stream().anyMatch(role -> "ROLE_PROPRIETAIRE".equals(role.getAuthority()))) {
            List<AdresseDTO> adressesAppartements = appartementService.obtenirAdressesAppartementsParUserId(auth.getName());
            return ResponseEntity.ok(adressesAppartements);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'avez pas les droits nécessaires pour accéder à ces informations.");
        }
    }
    @GetMapping("/chiffres-cles")
    public ResponseEntity<Object> obtenirCCAppartementsParUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<AppartementCCDTO> ccAppartements = appartementService.obtenirCCAppartementsParUserId(auth.getName());
        return ResponseEntity.ok(ccAppartements);
    }

    @GetMapping("/{apartmentId}")
    public ResponseEntity<?> obtenirUnAppartementParIdAndByUtilisateurId(@PathVariable Long apartmentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Appartement appartement = appartementService.obtenirUnAppartementParIdAndByUtilisateurId(apartmentId, auth.getName());

        if (appartement == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appartement not found");
        }
        if (auth.getAuthorities().stream().anyMatch(role -> "ROLE_PROPRIETAIRE".equals(role.getAuthority()))) {
            return ResponseEntity.ok(appartementService.convertToPrioprioDTO(appartement));
        } else if (auth.getAuthorities().stream().anyMatch(authority -> "ROLE_GESTIONNAIRE".equals(authority.getAuthority()))) {
            return ResponseEntity.ok(appartementService.convertToPrioprioDTO(appartement));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have the right to access this information.");
        }
    }
    @PutMapping("/{appartId}")
    public ResponseEntity<Appartement> mettreAJourUnAppartementPourUtilisateur(@PathVariable Long appartId, @RequestBody Appartement appartModifie) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            Appartement appartement = appartementService.mettreAJourUnAppartementPourUtilisateur(auth.getName(), appartId, appartModifie);
            return ResponseEntity.ok(appartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/ajouter")
    public ResponseEntity<Appartement> ajouterAppartement(@RequestBody Appartement nouvelAppartement) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Récupère le nom d'utilisateur ou email
        try {
            Appartement appartement = appartementService.ajouterAppartement(nouvelAppartement, email);
            return ResponseEntity.status(HttpStatus.CREATED).body(appartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{appartId}/gestionnaires")
    public ResponseEntity<List<UtilisateurDTO>> obtenirGestionnairesParAppartement(@PathVariable Long appartId) {

        List<UtilisateurDTO> appartGestionnaires = appartementService.obtenirGestionnairesParAppartement(appartId);
        if (appartGestionnaires.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appartGestionnaires);
    }
}
