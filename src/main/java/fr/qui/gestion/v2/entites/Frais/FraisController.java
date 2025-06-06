package fr.qui.gestion.v2.entites.Frais;
import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logements/{logementMasqueId}/frais")
public class FraisController {

    private final FraisService fraisService;

    public FraisController(FraisService fraisService) {
        this.fraisService = fraisService;
    }

    // ----------------------------------------------- //
    // Debut des frais concernant logement             //
    // ----------------------------------------------- //

    @GetMapping("/lister")
    public ResponseEntity<List<FraisDTO>> listerFraisPourLogement(@PathVariable String logementMasqueId) {
        List<FraisDTO> frais = fraisService.listerFraisPourLogement(logementMasqueId);
        return ResponseEntity.ok(frais);
    }
    @PostMapping("/ajouter")
    public ResponseEntity<FraisDTO> creerFraisPourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody FraisDTO fraisDTO) {
        FraisDTO nouveauFrais = fraisService.creerFraisPourLogement(logementMasqueId, fraisDTO);
        return ResponseEntity.ok(nouveauFrais);
    }
    @GetMapping("/{fraisMasqueId}/obtenir")
    public ResponseEntity<FraisDTO> obtenirFraisPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String fraisMasqueId) {
        FraisDTO frais = fraisService.obtenirFraisPourLogement(logementMasqueId, fraisMasqueId);
        return ResponseEntity.ok(frais);
    }
    @PatchMapping("/{fraisMasqueId}/modifier")
    public ResponseEntity<FraisDTO> modifierFraisPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String fraisMasqueId,
            @Valid @RequestBody FraisDTO fraisDTO) {
        FraisDTO fraisModifie = fraisService.modifierFraisPourLogement(logementMasqueId, fraisMasqueId, fraisDTO);
        return ResponseEntity.ok(fraisModifie);
    }
    @DeleteMapping("/{fraisMasqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimerFraisPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String fraisMasqueId) {
        return ResponseEntity.ok(fraisService.supprimerFraisPourLogement(logementMasqueId, fraisMasqueId));
    }

    // ----------------------------------------------- //
    // Debut des frais concernant periodes de location //
    // ----------------------------------------------- //

    @GetMapping("/periodes-de-location/{periodeMasqueId}/lister")
    public ResponseEntity<List<FraisDTO>> listerFraisPourPeriodeDeLocation(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeMasqueId) {
        List<FraisDTO> frais = fraisService.listerFraisPourPeriodeDeLocation(logementMasqueId, periodeMasqueId);
        return ResponseEntity.ok(frais);
    }
    @PostMapping("/periodes-de-location/{periodeMasqueId}/ajouter")
    public ResponseEntity<FraisDTO> creerFraisPourPeriodeDeLocation(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeMasqueId,
            @Valid @RequestBody FraisDTO fraisDTO) {
        FraisDTO nouveauFrais = fraisService.creerFraisPourPeriodeDeLocation(logementMasqueId, periodeMasqueId, fraisDTO);
        return ResponseEntity.ok(nouveauFrais);
    }
    @GetMapping("/{fraisMasqueId}/periodes-de-location/{periodeMasqueId}/obtenir")
    public ResponseEntity<FraisDTO> obtenirFraisPourPeriodeDeLocation(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeMasqueId,
            @PathVariable String fraisMasqueId) {
        FraisDTO frais = fraisService.obtenirFraisPourPeriodeDeLocation(logementMasqueId, periodeMasqueId, fraisMasqueId);
        return ResponseEntity.ok(frais);
    }
    @PatchMapping("/{fraisMasqueId}/periodes-de-location/{periodeMasqueId}/modifier")
    public ResponseEntity<FraisDTO> modifierFraisPourPeriodeDeLocation(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeMasqueId,
            @PathVariable String fraisMasqueId,
            @Valid @RequestBody FraisDTO fraisDTO) {
        FraisDTO fraisModifie = fraisService.modifierFraisPourPeriodeDeLocation(logementMasqueId, periodeMasqueId, fraisMasqueId, fraisDTO);
        return ResponseEntity.ok(fraisModifie);
    }
    @DeleteMapping("/{fraisMasqueId}/periodes-de-location/{periodeMasqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimerFraisPourPeriodeDeLocation(
            @PathVariable String logementMasqueId,
            @PathVariable String periodeMasqueId,
            @PathVariable String fraisMasqueId) {
        return ResponseEntity.ok(fraisService.supprimerFraisPourPeriodeDeLocation(logementMasqueId, periodeMasqueId, fraisMasqueId));
    }

    // ----------------------------------------------- //
    // Debut des frais concernant credit               //
    // ----------------------------------------------- //

    @GetMapping("/credit/{creditMasqueId}/lister")
    public ResponseEntity<List<FraisDTO>> listerFraisPourCredit(
            @PathVariable String logementMasqueId,
            @PathVariable String creditMasqueId) {
        List<FraisDTO> frais = fraisService.listerFraisPourCredit(logementMasqueId, creditMasqueId);
        return ResponseEntity.ok(frais);
    }
    @PostMapping("/credit/{creditMasqueId}/ajouter")
    public ResponseEntity<FraisDTO> creerFraisPourCredit(
            @PathVariable String logementMasqueId,
            @PathVariable String creditMasqueId,
            @Valid @RequestBody FraisDTO fraisDTO) {
        FraisDTO nouveauFrais = fraisService.creerFraisPourCredit(logementMasqueId, creditMasqueId, fraisDTO);
        return ResponseEntity.ok(nouveauFrais);
    }
    @GetMapping("/{fraisMasqueId}/credit/{creditMasqueId}/obtenir")
    public ResponseEntity<FraisDTO> obtenirFraisPourCredit(
            @PathVariable String logementMasqueId,
            @PathVariable String creditMasqueId,
            @PathVariable String fraisMasqueId) {
        FraisDTO frais = fraisService.obtenirFraisPourCredit(logementMasqueId, creditMasqueId, fraisMasqueId);
        return ResponseEntity.ok(frais);
    }
    @PatchMapping("/{fraisMasqueId}/credit/{creditMasqueId}/modifier")
    public ResponseEntity<FraisDTO> modifierFraisPourCredit(
            @PathVariable String logementMasqueId,
            @PathVariable String creditMasqueId,
            @PathVariable String fraisMasqueId,
            @Valid @RequestBody FraisDTO fraisDTO) {
        FraisDTO fraisModifie = fraisService.modifierFraisPourCredit(logementMasqueId, creditMasqueId, fraisMasqueId, fraisDTO);
        return ResponseEntity.ok(fraisModifie);
    }
    @DeleteMapping("/{fraisMasqueId}/credit/{creditMasqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimerFraisPourCredit(
            @PathVariable String logementMasqueId,
            @PathVariable String creditMasqueId,
            @PathVariable String fraisMasqueId) {
        return ResponseEntity.ok(fraisService.supprimerFraisPourCredit(logementMasqueId, creditMasqueId, fraisMasqueId));
    }
}
