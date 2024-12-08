package fr.qui.gestion.v2.entites.Photo;
import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logements/{logementMasqueId}/photos")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/lister")
    public ResponseEntity<List<PhotoDTO>> listerPhotos(@PathVariable String logementMasqueId) {
        List<PhotoDTO> photos = photoService.listerPhotos(logementMasqueId);
        return ResponseEntity.ok(photos);
    }

    @PostMapping("/ajouter")
    public ResponseEntity<PhotoDTO> creerPhotoPourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody PhotoDTO photoDTO) {
        PhotoDTO nouvellePhoto = photoService.creerPhotoPourLogement(logementMasqueId, photoDTO);
        return ResponseEntity.ok(nouvellePhoto);
    }

    @GetMapping("/{photoMasqueId}/obtenir")
    public ResponseEntity<PhotoDTO> obtenirPhotoPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String photoMasqueId) {
        PhotoDTO photo = photoService.obtenirPhotoPourLogement(logementMasqueId, photoMasqueId);
        return ResponseEntity.ok(photo);
    }
    @GetMapping("/principale")
    public ResponseEntity<PhotoDTO> obtenirPhotoPrincipalePourLogement(@PathVariable String logementMasqueId) {
        PhotoDTO photoPrincipale = photoService.obtenirPhotoPrincipalePourLogement(logementMasqueId);
        return ResponseEntity.ok(photoPrincipale);
    }

    @PatchMapping("/{photoMasqueId}/modifier")
    public ResponseEntity<PhotoDTO> modifierPhotoPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String photoMasqueId,
            @Valid @RequestBody PhotoDTO photoDTO) {
        PhotoDTO photoModifie = photoService.modifierPhotoPourLogement(logementMasqueId, photoMasqueId, photoDTO);
        return ResponseEntity.ok(photoModifie);
    }

    @DeleteMapping("/{photoMasqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimerPhotoPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String photoMasqueId) {
        return ResponseEntity.ok(photoService.supprimerPhotoPourLogement(logementMasqueId, photoMasqueId));
    }
}
