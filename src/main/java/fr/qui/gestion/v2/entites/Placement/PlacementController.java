package fr.qui.gestion.v2.entites.Placement;

import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/placements")
@CrossOrigin(origins = "${app.cors.origin}")
public class PlacementController {

    private final PlacementService placementService;

    public PlacementController(PlacementService placementService) {
        this.placementService = placementService;
    }

    @PostMapping("/ajouter")
    public ResponseEntity<PlacementVueEnsembleDTO> creer(@RequestBody PlacementVueEnsembleDTO dto) {
        return ResponseEntity.ok(placementService.creerPlacement(dto));
    }

    @PatchMapping("/modifier/{masqueId}")
    public ResponseEntity<PlacementVueEnsembleDTO> modifier(
            @PathVariable String masqueId,
            @RequestBody PlacementVueEnsembleDTO dto
    ) {
        return ResponseEntity.ok(placementService.modifierPlacement(masqueId, dto));
    }

    @GetMapping("/lister")
    public ResponseEntity<List<PlacementVueEnsembleDTO>> listerPlacements() {
        return ResponseEntity.ok(placementService.listerPlacements());
    }

    @GetMapping("/{masqueId}/obtenir")
    public ResponseEntity<PlacementVueEnsembleDTO> obtenirPlacement(@PathVariable String masqueId) {
        return ResponseEntity.ok(placementService.obtenirPlacement(masqueId));
    }

    @DeleteMapping("/{masqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimer(@PathVariable String masqueId) {
        return ResponseEntity.ok(placementService.supprimerPlacement(masqueId));
    }
}
