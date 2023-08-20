package fr.qui.gestion.typefrais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
