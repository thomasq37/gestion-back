package fr.qui.gestion.appart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
@CrossOrigin(origins = "https://gestion-front-7b998d8ddb5b.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:4200")
public class AppartementController {
    private final AppartementService appartementService;

    @Autowired
    public AppartementController(AppartementService appartementService) {
        this.appartementService = appartementService;
    }
    @GetMapping("/appartements")
    public List<Appartement> obtenirTousLesAppartements() {
        return appartementService.obtenirTousLesAppartements();
    }
    
    @GetMapping("/appartements/{id}")
    public Optional<Appartement> obtenirUnAppartementParId(@PathVariable("id") Long id) {
        return appartementService.findById(id);
    }
    

    @PostMapping("/{id}/calcul-rentabilite")
    public ResponseEntity<Double> calculerRentabiliteNette(@PathVariable("id") Long id) throws ClassNotFoundException {
        double rentabiliteNette = appartementService.calculerRentabiliteNette(id);
        return ResponseEntity.ok(rentabiliteNette);
    }
}
