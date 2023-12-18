package fr.qui.gestion.appart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "${app.cors.origin}")
public class PaysController {
	

    private final PaysService paysService;
    
    
    @Autowired
    public PaysController(PaysService paysService) {
        this.paysService = paysService;
    }

    @GetMapping("/api/pays")
    public ResponseEntity<List<Pays>> obtenirTousLesPays() {
    	List<Pays> pays = paysService.obtenirTousLesPays();
        return ResponseEntity.ok(pays);
    }
}
