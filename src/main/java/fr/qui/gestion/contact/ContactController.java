package fr.qui.gestion.contact;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/appartements/{appartId}/contacts", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class ContactController {
	
	private final ContactService contactService;
	    
    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // utilise v2 //

    @GetMapping()
    public ResponseEntity<List<Contact>> obtenirContactsAppartement(@PathVariable Long appartId) {

        List<Contact> contacts = contactService.obtenirContactsParAppartement(appartId);
        if(contacts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contacts);
    }

    @PostMapping()
    public ResponseEntity<Contact> ajouterUnContactPourAppartement(
            @PathVariable Long appartId,
            @RequestBody Contact newContact) {
        try {
            Contact contact = contactService.ajouterUnContactPourAppartement(appartId, newContact);
            return ResponseEntity.ok(contact);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<String> supprimerUnContactPourAppartement(@PathVariable Long appartId, @PathVariable Long contactId) {
        try {
            contactService.supprimerUnContactPourAppartement(contactId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/{contactId}")
    public ResponseEntity<Contact> mettreAJourUnContactPourAppartement(@PathVariable Long appartId, @PathVariable Long contactId, @RequestBody Contact contactMisAJour) {
        try {
            Contact contact = contactService.mettreAJourUnContactPourAppartement(appartId, contactId, contactMisAJour);
            return ResponseEntity.ok(contact);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}