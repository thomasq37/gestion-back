package fr.qui.gestion.appart;

import java.util.List;
import java.util.Optional;

import fr.qui.gestion.appart.dto.AdresseDTO;
import fr.qui.gestion.appart.dto.ChiffresClesDTO;
import fr.qui.gestion.user.appuser.AppUser;
import fr.qui.gestion.user.appuser.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import fr.qui.gestion.contact.Contact;
import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.user.appuser.AppUserDTO;

@RestController
@RequestMapping(path = "/api/utilisateurs/{userId}/appartements", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class AppartementController {

    private final AppartementService appartementService;
    private final AppUserService appUserService;

    @Autowired
    public AppartementController(AppartementService appartementService, AppUserService appUserService) {
        this.appartementService = appartementService;
        this.appUserService = appUserService;
    }

    // Appartements

    @PostMapping("/ajouter")
    public ResponseEntity<Appartement> ajouterAppartement(@RequestBody Appartement nouvelAppartement) {
        try {
            Appartement appartement = appartementService.ajouterAppartement(nouvelAppartement);
            return ResponseEntity.status(HttpStatus.CREATED).body(appartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{appartId}")
    public ResponseEntity<Appartement> mettreAJourUnAppartementPourUtilisateur(@PathVariable Long userId, @PathVariable Long appartId, @RequestBody Appartement appartModifie) {
        try {
            Appartement appartement = appartementService.mettreAJourUnAppartementPourUtilisateur(userId, appartId, appartModifie);
            return ResponseEntity.ok(appartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

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

    @GetMapping("/{appartId}/gestionnaires")
    public ResponseEntity<List<AppUserDTO>> obtenirGestionnairesParAppartement(@PathVariable Long userId, @PathVariable Long appartId) {

        List<AppUserDTO> appartGestionnaires = appartementService.obtenirGestionnairesParAppartement(appartId);
        if (appartGestionnaires.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appartGestionnaires);
    }

    @PutMapping("/{appartementId}/gestionnaires/{gestionnaireId}")
    public ResponseEntity<AppUserDTO> mettreAJourUnGestionnairePourAppartement(
            @PathVariable Long userId,
            @PathVariable Long appartementId,
            @PathVariable(required = false) Long gestionnaireId,
            @RequestBody AppUserDTO modifieGestionnaire) {
        try {
            AppUserDTO gestionnaire = appartementService.mettreAJourUnGestionnairePourAppartement(userId, appartementId, gestionnaireId, modifieGestionnaire);
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
    public ResponseEntity<Object> obtenirAdressesAppartementsParUserId(@PathVariable Long userId, HttpServletRequest request) {
        String userToken = request.getHeader("X-API-USER-KEY");
        Optional<AppUser> user = appUserService.findByUserToken(userToken);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide.");
        }
        AppUser currentUser = user.get();
        if (!currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Le token ne correspond pas à l'ID utilisateur fourni.");
        }
        // Si l'utilisateur est un gestionnaire
        if ("GESTIONNAIRE".equals(currentUser.getRole().getName())) {
            List<AdresseDTO> adressesAppartements = appartementService.obtenirAdressesAppartementsParGestionnaireId(currentUser.getId());
            return ResponseEntity.ok(adressesAppartements);
        }
        // Si l'utilisateur est un propriétaire
        else if ("PROPRIETAIRE".equals(currentUser.getRole().getName())) {
            List<AdresseDTO> adressesAppartements = appartementService.obtenirAdressesAppartementsParUserId(currentUser.getId());
            return ResponseEntity.ok(adressesAppartements);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'avez pas les droits nécessaires pour accéder à ces informations.");
        }
    }
    @GetMapping("/chiffres-cles")
    public ResponseEntity<Object> obtenirChiffresClesAppartementsParUserId(@PathVariable Long userId, HttpServletRequest request) {
        String userToken = request.getHeader("X-API-USER-KEY");
        Optional<AppUser> user = appUserService.findByUserToken(userToken);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide.");
        }
        AppUser currentUser = user.get();
        if (!currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Le token ne correspond pas à l'ID utilisateur fourni.");
        }
        List<ChiffresClesDTO> chiffresClesAppartements = appartementService.obtenirChiffresClesAppartementsParUserId(currentUser.getId());
        return ResponseEntity.ok(chiffresClesAppartements);
    }
}
