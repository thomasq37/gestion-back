package fr.qui.gestion.appart;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "${app.cors.origin}")
public class PaysController {

    @GetMapping("/api/pays")
    public Pays[] getPays() {
        return Pays.values();
    }
}
