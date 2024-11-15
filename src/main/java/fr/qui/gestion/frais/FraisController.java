package fr.qui.gestion.frais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(path = "/api/appartements/{appartId}", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class FraisController {
	
	private final FraisService fraisService;
	    
    @Autowired
    public FraisController(FraisService fraisService) {
        this.fraisService = fraisService;
    }
    
    @GetMapping("/periodes/{periodeId}/frais")
    public ResponseEntity<Page<Frais>> obtenirFraisFixesPourPeriode(@PathVariable Long appartId, @PathVariable Long periodeId, Pageable pageable) {
    	Page<Frais> frais = fraisService.obtenirFraisFixesPourPeriode(periodeId, pageable);
        if(frais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(frais);
    }

    // utilise v2 //
    @GetMapping("/frais")
    public ResponseEntity<Page<Frais>> obtenirFraisFixesPourAppartement(@PathVariable Long appartId, Pageable pageable) {
        Page<Frais> frais = fraisService.obtenirFraisFixesPourAppartement(appartId, pageable);
        if(frais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(frais);
    }
    @PutMapping("/frais/{fraisId}")
    public ResponseEntity<Frais> mettreAJourUnFraisPourAppartement(@PathVariable Long appartId, @PathVariable Long fraisId, @RequestBody Frais fraisMisAJour) {
        try {
            Frais frais = fraisService.mettreAJourUnFraisPourAppartement(appartId, fraisId, fraisMisAJour);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/frais/{fraisId}")
    public ResponseEntity<String> supprimerFraisPourAppartement(@PathVariable Long appartId, @PathVariable Long fraisId) {
        try {
            fraisService.supprimerFraisPourAppartement(fraisId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/frais")
    public ResponseEntity<Frais> ajouterUnFraisPourAppartement(@PathVariable Long appartId, @RequestBody Frais newFrais) {
        try {
            Frais frais = fraisService.ajouterUnFraisPourAppartement(appartId, newFrais);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/periodes/{periodeId}/frais/{fraisId}")
    public ResponseEntity<Frais> mettreAJourUnFraisPourPeriode(@PathVariable Long appartId, @PathVariable Long periodeId, @PathVariable Long fraisId, @RequestBody Frais fraisMisAJour) {
        try {
            Frais frais = fraisService.mettreAJourUnFraisPourPeriode(appartId, periodeId, fraisId, fraisMisAJour);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/periodes/{periodeId}/frais")
    public ResponseEntity<Frais> ajouterUnFraisPourPeriode(@PathVariable Long appartId, @PathVariable Long periodeId, @RequestBody Frais newFrais) {
        try {
            Frais frais = fraisService.ajouterUnFraisPourPeriode(appartId, periodeId, newFrais);
            return ResponseEntity.ok(frais);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}