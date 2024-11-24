package fr.qui.gestion.v2.entites.Caracteristiques;

import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logements/{logementMasqueId}/caracteristiques")
public class CaracteristiquesController {

    private final CaracteristiquesService caracteristiquesService;

    public CaracteristiquesController(CaracteristiquesService caracteristiquesService) {
        this.caracteristiquesService = caracteristiquesService;
    }

    @PostMapping("/ajouter")
    public ResponseEntity<CaracteristiquesDTO> creerCaracteristiquesPourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody CaracteristiquesDTO caracteristiquesDTO) {
        CaracteristiquesDTO nouvellesCaracteristiques = caracteristiquesService.creerCaracteristiquesPourLogement(logementMasqueId, caracteristiquesDTO);
        return ResponseEntity.ok(nouvellesCaracteristiques);
    }

    @GetMapping("/obtenir")
    public ResponseEntity<CaracteristiquesDTO> obtenirCaracteristiquesPourLogement(
            @PathVariable String logementMasqueId) {
        CaracteristiquesDTO caracteristiques = caracteristiquesService.obtenirCaracteristiquesPourLogement(logementMasqueId);
        return ResponseEntity.ok(caracteristiques);
    }

    @PatchMapping("/modifier")
    public ResponseEntity<CaracteristiquesDTO> modifierCaracteristiquesPourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody CaracteristiquesDTO caracteristiquesDTO) {
        CaracteristiquesDTO caracteristiquesModifiees = caracteristiquesService.modifierCaracteristiquesPourLogement(logementMasqueId, caracteristiquesDTO);
        return ResponseEntity.ok(caracteristiquesModifiees);
    }

    @DeleteMapping("/supprimer")
    public ResponseEntity<SuccessResponse> supprimerCaracteristiquesPourLogement(
            @PathVariable String logementMasqueId) {
        return ResponseEntity.ok(caracteristiquesService.supprimerCaracteristiquesPourLogement(logementMasqueId));

    }
}
