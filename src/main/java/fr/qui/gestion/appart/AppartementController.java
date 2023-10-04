package fr.qui.gestion.appart;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.qui.gestion.contact.Contact;
import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.user.AppUser;
import fr.qui.gestion.user.AppUserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/appartements", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class AppartementController {

    private final AppartementService appartementService;
    
    private final AppUserService appUserService;
    
    @Autowired
    public AppartementController(AppartementService appartementService, AppUserService appUserService) {
        this.appartementService = appartementService;
        this.appUserService = appUserService;
    }
    
    // Appartements
    
    @GetMapping("/liste")
    public List<Appartement> obtenirTousLesAppartements() {
        return appartementService.obtenirTousLesAppartements();
    }
    
    @PostMapping("/ajouter")
    public ResponseEntity<Appartement> ajouterAppartement(@RequestBody Appartement nouvelAppartement) {
        try {
            Appartement appartement = appartementService.ajouterAppartement(nouvelAppartement);
            return ResponseEntity.status(HttpStatus.CREATED).body(appartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/modifier/{id}")
    public ResponseEntity<Appartement> modifierAppartement(@PathVariable Long id, @RequestBody Appartement appartementModifie) {
        try {
            Appartement appartement = appartementService.modifierAppartement(id, appartementModifie);
            return ResponseEntity.ok(appartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Appartement> obtenirUnAppartementParId(@PathVariable("id") Long id, HttpServletRequest request) {
        try {
            // Extract the user token from the header
            String userToken = request.getHeader("X-API-USER-KEY");

            // Fetch the AppUser based on the user token
            Optional<AppUser> user = appUserService.findByUserToken(userToken);
            if(!user.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Token is invalid
            }

            Appartement appartement = appartementService.obtenirUnAppartementParId(id);

            // Check if the apartment belongs to the user
            if(!appartement.getAppUser().getId().equals(user.get().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Apartment doesn't belong to user
            }

            return ResponseEntity.ok(appartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> supprimerUnAppartement(@PathVariable Long id) {
    	appartementService.supprimerTousLesFraisParAppartementId(id);
    	appartementService.supprimerUnAppartement(id);
        return new ResponseEntity<>("Appartement deleted successfully", HttpStatus.OK);
    }
    
    @GetMapping("/adresses")
    public ResponseEntity<List<AdresseDTO>> obtenirToutesLesAdressesAppartements() {
    	try {
    		List<AdresseDTO> adresses = appartementService.obtenirToutesLesAdressesAppartements();
    		return ResponseEntity.ok(adresses) ;
    	} catch (IllegalArgumentException e) {
    		return ResponseEntity.notFound().build();
	    }
    }
    
    /*@GetMapping("/adresses/{userId}")
    public ResponseEntity<List<AdresseDTO>> obtenirAdressesAppartementsParUserId(@PathVariable Long userId) {
        try {
            List<AdresseDTO> adresses = appartementService.obtenirAdressesAppartementsParUserId(userId);
            if(adresses.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(adresses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
    @GetMapping("/adresses/{userToken}")
    public ResponseEntity<List<AdresseDTO>> obtenirAdressesAppartementsParUserToken(@PathVariable String userToken) {
        try {
            List<AdresseDTO> adresses = appartementService.obtenirAdressesAppartementsParUserToken(userToken);
            if(adresses.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(adresses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    
    // Frais
    
    @PutMapping("/{appartementId}/frais/{fraisId}")
    public ResponseEntity<Frais> mettreAJourUnFraisPourAppartement(@PathVariable Long appartementId, @PathVariable Long fraisId, @RequestBody Frais fraisMisAJour) {
        try {
            Frais frais = appartementService.mettreAJourUnFraisPourAppartement(appartementId, fraisId, fraisMisAJour);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{appartementId}/frais")
    public ResponseEntity<Frais> ajouterUnFraisPourAppartement(
            @PathVariable Long appartementId,
            @RequestBody Frais newFrais) {
        try {
            Frais frais = appartementService.ajouterUnFraisPourAppartement(appartementId, newFrais);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{appartementId}/frais/{fraisId}")
    @Transactional
    public ResponseEntity<?> supprimerFraisPourAppartement(@PathVariable Long appartementId, @PathVariable Long fraisId) {
        try {
            appartementService.supprimerFraisPourAppartement(appartementId, fraisId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Periode
    
    @PutMapping("/{appartementId}/periodes/{periodLocationId}")
    public ResponseEntity<PeriodLocation> mettreAJourUnePeriodePourAppartement(@PathVariable Long appartementId, @PathVariable Long periodLocationId, @RequestBody PeriodLocation periodLocationMisAJour) {
        try {
            PeriodLocation periodLocation = appartementService.mettreAJourUnePeriodePourAppartement(appartementId, periodLocationId, periodLocationMisAJour);
            return ResponseEntity.ok(periodLocation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{appartementId}/periodes")
    public ResponseEntity<PeriodLocation> ajouterUnePeriodePourAppartement(
            @PathVariable Long appartementId,
            @RequestBody PeriodLocation newPeriodLocation) {
        try {
        	PeriodLocation periodLocation = appartementService.ajouterUnePeriodePourAppartement(appartementId, newPeriodLocation);
            return ResponseEntity.ok(periodLocation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{appartementId}/periodes/{periodLocationId}")
    @Transactional
    public ResponseEntity<?> supprimerPeriodePourAppartement(@PathVariable Long appartementId, @PathVariable Long periodLocationId) {
        try {
            appartementService.supprimerPeriodePourAppartement(appartementId, periodLocationId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Frais Periodes
    
    @PostMapping("/periodes/{periodeId}/frais")
    public ResponseEntity<Frais> ajouterUnFraisPourPeriode(
            @PathVariable Long periodeId,
            @RequestBody Frais newFrais) {
        try {
            Frais frais = appartementService.ajouterUnFraisPourPeriode(periodeId, newFrais);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/periodes/{periodeId}/frais/{fraisId}")
    public ResponseEntity<Frais> mettreAJourUnFraisPourPeriode(@PathVariable Long periodeId, @PathVariable Long fraisId, @RequestBody Frais fraisMisAJour) {
        try {
            Frais frais = appartementService.mettreAJourUnFraisPourPeriode(periodeId, fraisId, fraisMisAJour);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/periodes/{periodeId}/frais/{fraisId}")
    @Transactional
    public ResponseEntity<?> supprimerFraisPourPeriode(@PathVariable Long periodeId, @PathVariable Long fraisId) {
        try {
            appartementService.supprimerFraisPourPeriode(periodeId, fraisId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Contacts
    
    @GetMapping("/{appartementId}/contacts")
    public ResponseEntity<List<Contact>> obtenirContactsPourAppartement(@PathVariable Long appartementId) {
        try {
            List<Contact> contacts = appartementService.obtenirContactsPourAppartement(appartementId);
            return ResponseEntity.ok(contacts);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    
    @PostMapping("/{appartementId}/contacts")
    public ResponseEntity<Contact> ajouterUnContactPourAppartement(
            @PathVariable Long appartementId,
            @RequestBody Contact newContact) {
        try {
        	Contact contact = appartementService.ajouterUnContactPourAppartement(appartementId, newContact);
            return ResponseEntity.ok(contact);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{appartementId}/contacts/{contactId}")
    public ResponseEntity<Contact> mettreAJourUnContactPourAppartement(@PathVariable Long appartementId, @PathVariable Long contactId, @RequestBody Contact contactMisAJour) {
        try {
        	Contact contact = appartementService.mettreAJourUnContactPourAppartement(appartementId, contactId, contactMisAJour);
            return ResponseEntity.ok(contact);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
