package fr.qui.gestion.v2.entites.Logement;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logements")
@CrossOrigin(origins = "${app.cors.origin}")
public class LogementController {

    private final LogementService logementService;

    public LogementController(LogementService logementService) {
        this.logementService = logementService;
    }

    @PostMapping("/ajouter")
    public ResponseEntity<LogementDTO> creerLogement() {
        LogementDTO nouveauLogement = logementService.creerLogement();
        return ResponseEntity.ok(nouveauLogement);
    }
    @GetMapping("/lister")
    public ResponseEntity<List<LogementDTO>> listerLogements() {
        List<LogementDTO> logements = logementService.listerLogements();
        return ResponseEntity.ok(logements);
    }
    @GetMapping("/{logementMasqueId}/obtenir")
    public ResponseEntity<LogementDTO> obtenirLogement(@PathVariable String logementMasqueId) {
        LogementDTO logement = logementService.obtenirLogement(logementMasqueId);
        return ResponseEntity.ok(logement);
    }
    @DeleteMapping("/{logementMasqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimerLogement(@PathVariable String logementMasqueId) {
        return ResponseEntity.ok(logementService.supprimerLogement(logementMasqueId));
    }
}