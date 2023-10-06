package fr.qui.gestion.frais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMessage;
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

@RestController
@RequestMapping(path = "/api/utilisateurs/{userId}", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class FraisController {
	
	private final FraisService fraisService;
	    
    @Autowired
    public FraisController(FraisService fraisService) {
        this.fraisService = fraisService;
    }
    
    @GetMapping("/appartements/{appartId}/frais")
    public ResponseEntity<List<Frais>> obtenirFraisFixesPourAppartement(@PathVariable Long userId, @PathVariable Long appartId) {
        List<Frais> frais = fraisService.obtenirFraisFixesPourAppartement(appartId);
        if(frais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(frais);
    }
    
    @GetMapping("/appartements/{appartId}/periodes/{periodeId}/frais")
    public ResponseEntity<List<Frais>> obtenirFraisFixesPourPeriode(@PathVariable Long userId, @PathVariable Long appartId, @PathVariable Long periodeId) {
        List<Frais> frais = fraisService.obtenirFraisFixesPourPeriode(periodeId);
        if(frais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(frais);
    }
    
    @PostMapping("/appartements/{appartId}/frais")
    public ResponseEntity<Frais> ajouterUnFraisPourAppartement(@PathVariable Long userId, @PathVariable Long appartId, @RequestBody Frais newFrais) {
        try {
            Frais frais = fraisService.ajouterUnFraisPourAppartement(appartId, newFrais);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/appartements/{appartId}/frais/{fraisId}")
    public ResponseEntity<String> supprimerFraisPourAppartement(@PathVariable Long userId, @PathVariable Long appartId, @PathVariable Long fraisId) {
        try {
            fraisService.supprimerFraisPourAppartement(fraisId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/appartements/{appartId}/frais/{fraisId}")
    public ResponseEntity<Frais> mettreAJourUnFraisPourAppartement(@PathVariable Long userId, @PathVariable Long appartId, @PathVariable Long fraisId, @RequestBody Frais fraisMisAJour) {
        try {
            Frais frais = fraisService.mettreAJourUnFraisPourAppartement(appartId, fraisId, fraisMisAJour);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
        	System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}