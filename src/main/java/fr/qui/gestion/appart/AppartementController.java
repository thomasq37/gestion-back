package fr.qui.gestion.appart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
@CrossOrigin(origins = "http://localhost:4200") // Remplacez par l'URL de votre application Angular
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
    

    @PostMapping("/{id}/calcul-rentabilite")
    public ResponseEntity<Double> calculerRentabiliteNette(@PathVariable("id") Long id) throws ClassNotFoundException {
        double rentabiliteNette = appartementService.calculerRentabiliteNette(id);
        return ResponseEntity.ok(rentabiliteNette);
    }
}
