package fr.qui.gestion.appart;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public Optional<Appartement> obtenirUnAppartementParId(@PathVariable("id") Long id) {
        return appartementService.findById(id);
    }
    

    @PostMapping("/{id}/calcul-rentabilite")
    public ResponseEntity<Double> calculerRentabiliteNette(@PathVariable("id") Long id) throws ClassNotFoundException {
        double rentabiliteNette = appartementService.calculerRentabiliteNette(id);
        return ResponseEntity.ok(rentabiliteNette);
    }
    
    @PostMapping("/{id}/moyenne-benefices")
    public ResponseEntity<Double> calculerMoyenneBenefices(@PathVariable("id") Long id) throws ClassNotFoundException {
        double moyenneBenefices = appartementService.calculerMoyenneBenefices(id);
        return ResponseEntity.ok(moyenneBenefices);
    }
    
    @GetMapping("/{id}/taux-vacances-locatives")
    public  ResponseEntity<Double> calculerTauxVacanceLocativeMoyen(@PathVariable("id") Long id) throws ClassNotFoundException {
        double tauxVacancesLocatives = appartementService.calculerTauxVacancesLocatives(id);
        return ResponseEntity.ok(tauxVacancesLocatives);
    }
}
