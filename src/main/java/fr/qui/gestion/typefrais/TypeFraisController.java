package fr.qui.gestion.typefrais;

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
@RequestMapping(path = "/api/type-frais", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class TypeFraisController {

private final TypeFraisService typeFraisService;
    
    @Autowired
    public TypeFraisController(TypeFraisService typeFraisService) {
        this.typeFraisService = typeFraisService;
    }
    
    @GetMapping("/liste")
    public ResponseEntity<List<TypeFrais>> obtenirTousLesTypeDeFrais() {
        List<TypeFrais> fraisList = typeFraisService.obtenirTousLesTypeDeFrais();
        return ResponseEntity.ok(fraisList);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TypeFrais> obtenirUnTypeDeFraisParId(@PathVariable("id") Long id) {
        try {
        	TypeFrais typeDeFrais = typeFraisService.obtenirUnTypeDeFraisParId(id);
            return ResponseEntity.ok(typeDeFrais);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @PostMapping("/ajouter")
    public ResponseEntity<TypeFrais> ajouterTypeDeFrais(@RequestBody TypeFrais nouveauTypeDeFrais) {
        try {
        	TypeFrais typeFrais = typeFraisService.ajouterTypeDeFrais(nouveauTypeDeFrais);
            return ResponseEntity.status(HttpStatus.CREATED).body(typeFrais);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerUnTypeDeFrais(@PathVariable Long id) {
    	typeFraisService.supprimerTypeDeFrais(id);
        return new ResponseEntity<>("Type de frais deleted successfully", HttpStatus.OK);
    }
    
  
    @PutMapping("/modifier/{id}")
    public ResponseEntity<TypeFrais> modifierTypeFrais(@PathVariable Long id, @RequestBody TypeFrais typeFraisModifie) {
        try {
        	TypeFrais typeFrais = typeFraisService.modifierTypeFrais(id, typeFraisModifie);
            return ResponseEntity.ok(typeFrais);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
