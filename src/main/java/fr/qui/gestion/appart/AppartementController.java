package fr.qui.gestion.appart;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/appartements", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")

public class AppartementController {
    private final AppartementService appartementService;
    
    @Autowired
    public AppartementController(AppartementService appartementService) {
        this.appartementService = appartementService;
    }
    
    @GetMapping("/list")
    public List<Appartement> obtenirTousLesAppartements() {
        return appartementService.obtenirTousLesAppartements();
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
