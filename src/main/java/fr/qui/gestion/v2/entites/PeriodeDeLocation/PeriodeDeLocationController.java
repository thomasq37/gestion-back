package fr.qui.gestion.v2.entites.PeriodeDeLocation;
import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logements/{logementMasqueId}/periodes-de-location")
public class PeriodeDeLocationController {

    private final PeriodeDeLocationService periodeDeLocationService;

    public PeriodeDeLocationController(PeriodeDeLocationService periodeDeLocationService) {
        this.periodeDeLocationService = periodeDeLocationService;
    }

    @GetMapping("/lister")
    public ResponseEntity<List<PeriodeDeLocationDTO>> listerPeriodeDeLocations(@PathVariable String logementMasqueId) {
        List<PeriodeDeLocationDTO> periodeDeLocations = periodeDeLocationService.listerPeriodeDeLocations(logementMasqueId);
        return ResponseEntity.ok(periodeDeLocations);
    }

    @PostMapping("/ajouter")
    public ResponseEntity<PeriodeDeLocationDTO> creerPeriodeDeLocationPourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody PeriodeDeLocationDTO periodeDeLocationDTO) {
        PeriodeDeLocationDTO nouvellePeriodeDeLocation = periodeDeLocationService.creerPeriodeDeLocationPourLogement(logementMasqueId, periodeDeLocationDTO);
        return ResponseEntity.ok(nouvellePeriodeDeLocation);
    }

    @GetMapping("/{periodeDeLocationMasqueId}/obtenir")
    public ResponseEntity<PeriodeDeLocationDTO> obtenirPeriodeDeLocationPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeDeLocationMasqueId) {
        PeriodeDeLocationDTO periodeDeLocation = periodeDeLocationService.obtenirPeriodeDeLocationPourLogement(logementMasqueId, periodeDeLocationMasqueId);
        return ResponseEntity.ok(periodeDeLocation);
    }

    @PatchMapping("/{periodeDeLocationMasqueId}/modifier")
    public ResponseEntity<PeriodeDeLocationDTO> modifierPeriodeDeLocationPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeDeLocationMasqueId,
            @Valid @RequestBody PeriodeDeLocationDTO periodeDeLocationDTO) {
        PeriodeDeLocationDTO periodeDeLocationModifie = periodeDeLocationService.modifierPeriodeDeLocationPourLogement(logementMasqueId, periodeDeLocationMasqueId, periodeDeLocationDTO);
        return ResponseEntity.ok(periodeDeLocationModifie);
    }

    @DeleteMapping("/{periodeDeLocationMasqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimerPeriodeDeLocationPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeDeLocationMasqueId) {
        return ResponseEntity.ok(periodeDeLocationService.supprimerPeriodeDeLocationPourLogement(logementMasqueId, periodeDeLocationMasqueId));
    }
}
