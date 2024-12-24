package fr.qui.gestion.v2.entites.Alerte;
import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logements/{logementMasqueId}/alertes")
public class AlerteController {

    private final AlerteService alerteService;

    public AlerteController(AlerteService alerteService) {
        this.alerteService = alerteService;
    }

    @GetMapping("/lister")
    public ResponseEntity<List<AlerteDTO>> listerAlertes(@PathVariable String logementMasqueId) {
        List<AlerteDTO> alertes = alerteService.listerAlertes(logementMasqueId);
        return ResponseEntity.ok(alertes);
    }

    @PostMapping("/ajouter")
    public ResponseEntity<AlerteDTO> creerAlertePourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody AlerteDTO alerteDTO) {
        AlerteDTO nouvelleAlerte = alerteService.creerAlertePourLogement(logementMasqueId, alerteDTO);
        return ResponseEntity.ok(nouvelleAlerte);
    }

    @GetMapping("/{alerteMasqueId}/obtenir")
    public ResponseEntity<AlerteDTO> obtenirAlertePourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String alerteMasqueId) {
        AlerteDTO alerte = alerteService.obtenirAlertePourLogement(logementMasqueId, alerteMasqueId);
        return ResponseEntity.ok(alerte);
    }

    @PatchMapping("/{alerteMasqueId}/modifier")
    public ResponseEntity<AlerteDTO> modifierAlertePourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String alerteMasqueId,
            @Valid @RequestBody AlerteDTO alerteDTO) {
        AlerteDTO alerteModifie = alerteService.modifierAlertePourLogement(logementMasqueId, alerteMasqueId, alerteDTO);
        return ResponseEntity.ok(alerteModifie);
    }

    @DeleteMapping("/{alerteMasqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimerAlertePourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String alerteMasqueId) {
        return ResponseEntity.ok(alerteService.supprimerAlertePourLogement(logementMasqueId, alerteMasqueId));
    }
}
