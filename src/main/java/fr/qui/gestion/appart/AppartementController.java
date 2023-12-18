package fr.qui.gestion.appart;

import java.util.List;

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

import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.user.AppUserDTO;

@RestController
@RequestMapping(path = "/api/utilisateurs/{userId}/appartements", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class AppartementController {

    private final AppartementService appartementService;
    
    
    @Autowired
    public AppartementController(AppartementService appartementService) {
        this.appartementService = appartementService;
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
        if(appartGestionnaires.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appartGestionnaires);
    }
}
