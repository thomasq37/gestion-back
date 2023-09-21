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

import fr.qui.gestion.frais.FraisService;

@RestController
@RequestMapping(path = "/api/appartements", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")

public class AppartementController {

    private final AppartementService appartementService;
    private final FraisService fraisService;
    @Autowired
    public AppartementController(AppartementService appartementService, FraisService fraisService) {
        this.appartementService = appartementService;
		this.fraisService = fraisService;
    }
    
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
    	fraisService.supprimerTousLesFraisParAppartementId(id);
    	appartementService.supprimerUnAppartement(id);
        return new ResponseEntity<>("Appartement deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}/calcul-rentabilite")
    public ResponseEntity<Double> calculerRentabiliteNette(@PathVariable("id") Long id) {
        try {
            double rentabiliteNette = appartementService.calculerRentabiliteNette(id);
            return ResponseEntity.ok(rentabiliteNette);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/moyenne-benefices")
    public ResponseEntity<Double> calculerMoyenneBenefices(@PathVariable("id") Long id) {
        try {
            double moyenneBenefices = appartementService.calculerMoyenneBenefices(id);
            return ResponseEntity.ok(moyenneBenefices);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/taux-vacances-locatives")
    public ResponseEntity<Double> calculerTauxVacancesLocativeMoyen(@PathVariable("id") Long id) {
        try {
            double tauxVacancesLocatives = appartementService.calculerTauxVacancesLocatives(id);
            return ResponseEntity.ok(tauxVacancesLocatives);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
