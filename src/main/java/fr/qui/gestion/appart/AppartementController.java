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
import fr.qui.gestion.periodlocation.PeriodLocation;

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
    
    @GetMapping("/liste")
    public List<Appartement> obtenirTousLesAppartements() {
        return appartementService.obtenirTousLesAppartements();
    }
    
    @PostMapping("/ajouter")
    public ResponseEntity<Appartement> ajouterAppartement(@RequestBody Appartement nouvelAppartement) {
        try {
            Appartement appartement = appartementService.ajouterAppartement(nouvelAppartement);
            return ResponseEntity.status(HttpStatus.CREATED).body(appartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/modifier/{id}")
    public ResponseEntity<Appartement> modifierAppartement(@PathVariable Long id, @RequestBody Appartement appartementModifie) {
        try {
            Appartement appartement = appartementService.modifierAppartement(id, appartementModifie);
            return ResponseEntity.ok(appartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Appartement> obtenirUnAppartementParId(@PathVariable("id") Long id) {
        try {
            Appartement appartement = appartementService.obtenirUnAppartementParId(id);
            return ResponseEntity.ok(appartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> supprimerUnAppartement(@PathVariable Long id) {
    	appartementService.supprimerTousLesFraisParAppartementId(id);
    	appartementService.supprimerUnAppartement(id);
        return new ResponseEntity<>("Appartement deleted successfully", HttpStatus.OK);
    }
    
    @GetMapping("/adresses")
    public ResponseEntity<List<AdresseDTO>> obtenirToutesLesAdressesAppartements() {
    	try {
    		List<AdresseDTO> adresses = appartementService.obtenirToutesLesAdressesAppartements();
    		return ResponseEntity.ok(adresses) ;
    	} catch (IllegalArgumentException e) {
    		return ResponseEntity.notFound().build();
	    }
    }
    
    
    // Frais
    
    @PutMapping("/{appartementId}/frais/{fraisId}")
    public ResponseEntity<Frais> mettreAJourUnFraisPourAppartement(@PathVariable Long appartementId, @PathVariable Long fraisId, @RequestBody Frais fraisMisAJour) {
        try {
            Frais frais = appartementService.mettreAJourUnFraisPourAppartement(appartementId, fraisId, fraisMisAJour);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{appartementId}/frais")
    public ResponseEntity<Frais> ajouterUnFraisPourAppartement(
            @PathVariable Long appartementId,
            @RequestBody Frais newFrais) {
        try {
            Frais frais = appartementService.ajouterUnFraisPourAppartement(appartementId, newFrais);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{appartementId}/frais/{fraisId}")
    @Transactional
    public ResponseEntity<?> supprimerFraisPourAppartement(@PathVariable Long appartementId, @PathVariable Long fraisId) {
        try {
            appartementService.supprimerFraisPourAppartement(appartementId, fraisId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Periode
    
    @PutMapping("/{appartementId}/periodes/{periodLocationId}")
    public ResponseEntity<PeriodLocation> mettreAJourUnePeriodePourAppartement(@PathVariable Long appartementId, @PathVariable Long periodLocationId, @RequestBody PeriodLocation periodLocationMisAJour) {
        try {
            PeriodLocation periodLocation = appartementService.mettreAJourUnePeriodePourAppartement(appartementId, periodLocationId, periodLocationMisAJour);
            return ResponseEntity.ok(periodLocation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{appartementId}/periodes")
    public ResponseEntity<PeriodLocation> ajouterUnePeriodePourAppartement(
            @PathVariable Long appartementId,
            @RequestBody PeriodLocation newPeriodLocation) {
        try {
        	PeriodLocation periodLocation = appartementService.ajouterUnePeriodePourAppartement(appartementId, newPeriodLocation);
            return ResponseEntity.ok(periodLocation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{appartementId}/periodes/{periodLocationId}")
    @Transactional
    public ResponseEntity<?> supprimerPeriodePourAppartement(@PathVariable Long appartementId, @PathVariable Long periodLocationId) {
        try {
            appartementService.supprimerPeriodePourAppartement(appartementId, periodLocationId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/{appartementId}/periodes/{periodeId}/frais")
    public ResponseEntity<Frais> ajouterUnFraisPourPeriode(
            @PathVariable Long appartementId,
            @PathVariable Long periodeId,
            @RequestBody Frais newFrais) {
        try {
            Frais frais = appartementService.ajouterUnFraisPourPeriode(appartementId, periodeId, newFrais);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
