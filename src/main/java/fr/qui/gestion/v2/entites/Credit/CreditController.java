package fr.qui.gestion.v2.entites.Credit;

import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logements/{logementMasqueId}/credit")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/ajouter")
    public ResponseEntity<CreditDTO> creerCreditPourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody CreditDTO creditDTO) {
        CreditDTO nouvellesCredit = creditService.creerCreditPourLogement(logementMasqueId, creditDTO);
        return ResponseEntity.ok(nouvellesCredit);
    }

    @GetMapping("/obtenir")
    public ResponseEntity<CreditDTO> obtenirCreditPourLogement(
            @PathVariable String logementMasqueId) {
        CreditDTO credit = creditService.obtenirCreditPourLogement(logementMasqueId);
        return ResponseEntity.ok(credit);
    }

    @PatchMapping("/modifier")
    public ResponseEntity<CreditDTO> modifierCreditPourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody CreditDTO creditDTO) {
        CreditDTO creditModifiees = creditService.modifierCreditPourLogement(logementMasqueId, creditDTO);
        return ResponseEntity.ok(creditModifiees);
    }

    @DeleteMapping("/supprimer")
    public ResponseEntity<SuccessResponse> supprimerCreditPourLogement(
            @PathVariable String logementMasqueId) {
        return ResponseEntity.ok(creditService.supprimerCreditPourLogement(logementMasqueId));

    }
}
