package fr.qui.gestion.mouvementappart;

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

import fr.qui.gestion.frais.Frais;

@RestController
@RequestMapping(path = "/api/mouvements", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class MouvementAppartementController {
	private final MouvementAppartementservice mouvementAppartementservice;
	
	@Autowired
    public MouvementAppartementController(MouvementAppartementservice mouvementAppartementservice) {
        this.mouvementAppartementservice = mouvementAppartementservice;
    }
	
    @GetMapping("/liste")
    public List<MouvementAppartement> obtenirTousLesMouvAppartements() {
        return mouvementAppartementservice.obtenirTousLesMouvAppartements();
    }
    
    @PutMapping("/modifier/{id}")
    public ResponseEntity<MouvementAppartement> modifierMouvementAppartement(@PathVariable Long id, @RequestBody MouvementAppartement mouvementAppartementModifie) {
        try {
            MouvementAppartement mouvementAppartement = mouvementAppartementservice.modifierMouvementAppartement(id, mouvementAppartementModifie);
            return ResponseEntity.ok(mouvementAppartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerUnMouvement(@PathVariable Long id) {
    	mouvementAppartementservice.supprimerUnMouvement(id);
        return new ResponseEntity<>("Mouvement deleted successfully", HttpStatus.OK);
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<MouvementAppartement> obtenirUnMouvAppartementParId(@PathVariable("id") Long id) {
        try {
        	MouvementAppartement mouvementAppartement = mouvementAppartementservice.obtenirUnMouvAppartementParId(id);
            return ResponseEntity.ok(mouvementAppartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/ajouter")
    public ResponseEntity<MouvementAppartement> ajouterMouvementAppartement(@RequestBody MouvementAppartement nouveauMouvementAppartement) {
        try {
        	MouvementAppartement mouvementAppartement = mouvementAppartementservice.ajouterMouvementAppartement(nouveauMouvementAppartement);
            return ResponseEntity.status(HttpStatus.CREATED).body(mouvementAppartement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
