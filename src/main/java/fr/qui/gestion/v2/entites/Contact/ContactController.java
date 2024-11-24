package fr.qui.gestion.v2.entites.Contact;
import fr.qui.gestion.v2.exception.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logements/{logementMasqueId}/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/lister")
    public ResponseEntity<List<ContactDTO>> listerContacts(@PathVariable String logementMasqueId) {
        List<ContactDTO> contacts = contactService.listerContacts(logementMasqueId);
        return ResponseEntity.ok(contacts);
    }

    @PostMapping("/ajouter")
    public ResponseEntity<ContactDTO> creerContactPourLogement(
            @PathVariable String logementMasqueId,
            @Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO nouveauContact = contactService.creerContactPourLogement(logementMasqueId, contactDTO);
        return ResponseEntity.ok(nouveauContact);
    }

    @GetMapping("/{contactMasqueId}/obtenir")
    public ResponseEntity<ContactDTO> obtenirContactPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String contactMasqueId) {
        ContactDTO contact = contactService.obtenirContactPourLogement(logementMasqueId, contactMasqueId);
        return ResponseEntity.ok(contact);
    }

    @PatchMapping("/{contactMasqueId}/modifier")
    public ResponseEntity<ContactDTO> modifierContactPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String contactMasqueId,
            @Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO contactModifie = contactService.modifierContactPourLogement(logementMasqueId, contactMasqueId, contactDTO);
        return ResponseEntity.ok(contactModifie);
    }

    @DeleteMapping("/{contactMasqueId}/supprimer")
    public ResponseEntity<SuccessResponse> supprimerContactPourLogement(
            @PathVariable String logementMasqueId,
            @PathVariable String contactMasqueId) {
        return ResponseEntity.ok(contactService.supprimerContactPourLogement(logementMasqueId, contactMasqueId));
    }
}
