package fr.qui.gestion.v2.entites.Adresse;
import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logements/{logementMasqueId}/adresse")
public class AdresseController {

    private final AdresseService adresseService;

    public AdresseController(AdresseService adresseService) {
        this.adresseService = adresseService;
    }

    @PostMapping("/ajouter")
    public ResponseEntity<AdresseDTO> creerAdressePourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody AdresseDTO adresseDTO) {
        AdresseDTO nouvelleAdresse = adresseService.creerAdressePourLogement(logementMasqueId, adresseDTO);
        return ResponseEntity.ok(nouvelleAdresse);
    }

    @GetMapping("/obtenir")
    public ResponseEntity<AdresseDTO> obtenirAdressePourLogement(
            @PathVariable String logementMasqueId) {
        AdresseDTO adresse = adresseService.obtenirAdressePourLogement(logementMasqueId);
        return ResponseEntity.ok(adresse);
    }

    @PatchMapping("/modifier")
    public ResponseEntity<AdresseDTO> modifierAdressePourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody AdresseDTO adresseDTO) {
        AdresseDTO adresseModifiee = adresseService.modifierAdressePourLogement(logementMasqueId, adresseDTO);
        return ResponseEntity.ok(adresseModifiee);
    }

    @DeleteMapping("/supprimer")
    public ResponseEntity<SuccessResponse> supprimerAdressePourLogement(
            @PathVariable String logementMasqueId) {
        return ResponseEntity.ok(adresseService.supprimerAdressePourLogement(logementMasqueId));
    }
}
