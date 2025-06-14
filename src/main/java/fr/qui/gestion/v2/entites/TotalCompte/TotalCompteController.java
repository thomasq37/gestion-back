package fr.qui.gestion.v2.entites.TotalCompte;

import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/total-compte")
@CrossOrigin(origins = "${app.cors.origin}")
public class TotalCompteController {

    private final TotalCompteService totalCompteService;

    public TotalCompteController(TotalCompteService totalCompteService) {
        this.totalCompteService = totalCompteService;
    }

    @PostMapping("/ajouter")
    public ResponseEntity<TotalCompteDTO> creer(@RequestBody TotalCompteDTO dto) {
        return ResponseEntity.ok(totalCompteService.creerTotalCompte(dto));
    }

    @PatchMapping("/modifier/{masqueId}")
    public ResponseEntity<TotalCompteDTO> modifier(
            @PathVariable String masqueId,
            @RequestBody TotalCompteDTO dto
    ) {
        return ResponseEntity.ok(totalCompteService.modifierTotalCompte(masqueId, dto));
    }

    @GetMapping("/lister")
    public ResponseEntity<List<TotalCompteDTO>> listerTotalComptes() {
        return ResponseEntity.ok(totalCompteService.listerTotalComptes());
    }

    @GetMapping("/{masqueId}/obtenir")
    public ResponseEntity<TotalCompteDTO> obtenirTotalCompte(@PathVariable String masqueId) {
        return ResponseEntity.ok(totalCompteService.obtenirTotalCompte(masqueId));
    }

    @DeleteMapping("/{masqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimer(@PathVariable String masqueId) {
        return ResponseEntity.ok(totalCompteService.supprimerTotalCompte(masqueId));
    }
}
