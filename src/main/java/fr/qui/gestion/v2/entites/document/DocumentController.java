package fr.qui.gestion.v2.entites.document;

import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logements/{logementMasqueId}/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/lister")
    public ResponseEntity<List<DocumentDTO>> listerDocuments(@PathVariable String logementMasqueId) {
        List<DocumentDTO> documents = documentService.listerDocuments(logementMasqueId);
        return ResponseEntity.ok(documents);
    }

    @PostMapping("/ajouter")
    public ResponseEntity<DocumentDTO> ajouterDocument(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody DocumentDTO documentDTO) {
        DocumentDTO nouveauDocument = documentService.ajouterDocument(logementMasqueId, documentDTO);
        return ResponseEntity.ok(nouveauDocument);
    }

    @GetMapping("/{documentMasqueId}/obtenir")
    public ResponseEntity<DocumentDTO> obtenirDocument(@PathVariable String documentMasqueId) {
        DocumentDTO document = documentService.obtenirDocument(documentMasqueId);
        return ResponseEntity.ok(document);
    }

    @DeleteMapping("/{documentMasqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimerDocument(
            @PathVariable String logementMasqueId,
            @PathVariable String documentMasqueId) {
        SuccessResponse response = documentService.supprimerDocument(logementMasqueId, documentMasqueId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/associer/{documentMasqueId}")
    public ResponseEntity<SuccessResponse> associerDocumentExistant(
            @PathVariable String logementMasqueId,
            @PathVariable String documentMasqueId) {
        SuccessResponse response = documentService.associerDocumentExistant(logementMasqueId, documentMasqueId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/disponibles")
    public ResponseEntity<List<DocumentDTO>> listerDocumentsDisponibles() {
        List<DocumentDTO> documents = documentService.listerDocumentsDisponiblesPourUtilisateur();
        return ResponseEntity.ok(documents);
    }
}
