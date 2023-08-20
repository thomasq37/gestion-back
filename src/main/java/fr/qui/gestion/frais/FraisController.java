package fr.qui.gestion.frais;

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
@RequestMapping(path = "/api/frais", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class FraisController {

private final FraisService fraisService;
    
    @Autowired
    public FraisController(FraisService fraisService) {
        this.fraisService = fraisService;
    }
    
    @GetMapping("/liste")
    public ResponseEntity<List<Frais>> obtenirTousLesFrais() {
        List<Frais> fraisList = fraisService.obtenirTousLesFrais();
        return ResponseEntity.ok(fraisList);
    }
    
    @GetMapping("/appartement/{appartementId}")
    public ResponseEntity<List<Frais>> obtenirTousLesFraisParAppartementId(@PathVariable Long appartementId) {
        List<Frais> fraisList = fraisService.obtenirTousLesFraisParAppartementId(appartementId);
        return ResponseEntity.ok(fraisList);
    }
    
    @PostMapping("/ajouter")
    public ResponseEntity<Frais> ajouterFrais(@RequestBody Frais nouveauFrais) {
        try {
        	Frais frais = fraisService.ajouterFrais(nouveauFrais);
            return ResponseEntity.status(HttpStatus.CREATED).body(frais);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/modifier/{id}")
    public ResponseEntity<Frais> modifierFrais(@PathVariable Long id, @RequestBody Frais fraisModifie) {
        try {
            Frais frais = fraisService.modifierFrais(id, fraisModifie);
            return ResponseEntity.ok(frais);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Frais> obtenirUnFraisParId(@PathVariable("id") Long id) {
        try {
        	Frais frais = fraisService.obtenirUnFraisParId(id);
            return ResponseEntity.ok(frais);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerUnFrais(@PathVariable Long id) {
        fraisService.supprimerUnFrais(id);
        return new ResponseEntity<>("Frais deleted successfully", HttpStatus.OK);
    }
    
}
