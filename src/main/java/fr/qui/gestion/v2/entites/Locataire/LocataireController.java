package fr.qui.gestion.v2.entites.Locataire;
import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logements/{logementMasqueId}/locataires")
public class LocataireController {

    private final LocataireService locataireService;

    public LocataireController(LocataireService locataireService) {
        this.locataireService = locataireService;
    }

    @GetMapping("/periodes-de-location/{periodeMasqueId}/lister")
    public ResponseEntity<List<LocataireDTO>> listerLocatairesPourPeriodeDeLocation(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeMasqueId) {
        List<LocataireDTO> locataires = locataireService.listerLocatairesPourPeriodeDeLocation(logementMasqueId, periodeMasqueId);
        return ResponseEntity.ok(locataires);
    }

    @PostMapping("/periodes-de-location/{periodeMasqueId}/ajouter")
    public ResponseEntity<LocataireDTO> creerLocatairePourPeriodeDeLocation(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeMasqueId,
            @Valid @RequestBody LocataireDTO locataireDTO) {
        LocataireDTO nouveauLocataire = locataireService.creerLocatairePourPeriodeDeLocation(logementMasqueId, periodeMasqueId, locataireDTO);
        return ResponseEntity.ok(nouveauLocataire);
    }

    @GetMapping("/{locataireMasqueId}/periodes-de-location/{periodeMasqueId}/obtenir")
    public ResponseEntity<LocataireDTO> obtenirLocatairePourPeriodeDeLocation(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeMasqueId,
            @PathVariable String locataireMasqueId) {
        LocataireDTO locataire = locataireService.obtenirLocatairePourPeriodeDeLocation(logementMasqueId, periodeMasqueId, locataireMasqueId);
        return ResponseEntity.ok(locataire);
    }

    @PatchMapping("/{locataireMasqueId}/periodes-de-location/{periodeMasqueId}/modifier")
    public ResponseEntity<LocataireDTO> modifierLocatairePourPeriodeDeLocation(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeMasqueId,
            @PathVariable String locataireMasqueId,
            @Valid @RequestBody LocataireDTO locataireDTO) {
        LocataireDTO locataireModifie = locataireService.modifierLocatairePourPeriodeDeLocation(logementMasqueId, periodeMasqueId, locataireMasqueId, locataireDTO);
        return ResponseEntity.ok(locataireModifie);
    }

    @DeleteMapping("/{locataireMasqueId}/periodes-de-location/{periodeMasqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimerLocatairePourPeriodeDeLocation(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeMasqueId,
            @PathVariable String locataireMasqueId) {
        return ResponseEntity.ok(locataireService.supprimerLocatairePourPeriodeDeLocation(logementMasqueId, periodeMasqueId, locataireMasqueId));
    }
}
