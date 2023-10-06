package fr.qui.gestion.periodlocation;

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
@RequestMapping(path = "/api/utilisateurs/{userId}/appartements/{appartId}/periodes", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class PeriodLocationController {
	
	private final PeriodLocationService periodLocationService;
	    
    @Autowired
    public PeriodLocationController(PeriodLocationService periodLocationService) {
        this.periodLocationService = periodLocationService;
    }

    @GetMapping()
    public ResponseEntity<List<PeriodLocation>> obtenirPeriodeLocationParAppartement(@PathVariable Long userId, @PathVariable Long appartId) {
		
    	List<PeriodLocation> periodLocations = periodLocationService.obtenirPeriodeLocationParAppartement(appartId); 
        if(periodLocations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(periodLocations);
    }
 
    @PostMapping()
    public ResponseEntity<PeriodLocation> ajouterUnePeriodeLocationPourAppartement(
            @PathVariable Long appartId,
            @RequestBody PeriodLocation newPeriodLocation) {
        try {
        	PeriodLocation periodLocation = periodLocationService.ajouterUnePeriodeLocationPourAppartement(appartId, newPeriodLocation);
            return ResponseEntity.ok(periodLocation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{periodeId}")
    public ResponseEntity<PeriodLocation> mettreAJourUnePeriodePourAppartement(@PathVariable Long userId, @PathVariable Long appartId, @PathVariable Long periodeId, @RequestBody PeriodLocation periodeMisAJour) {
        try {
        	PeriodLocation periodLocation = periodLocationService.mettreAJourUnePeriodePourAppartement(appartId, periodeId, periodeMisAJour);
            return ResponseEntity.ok(periodLocation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{periodeId}")
    public ResponseEntity<String> supprimerUnePeriodePourAppartement(@PathVariable Long userId, @PathVariable Long appartId, @PathVariable Long periodeId) {
        try {
            periodLocationService.supprimerUnePeriodePourAppartement(periodeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}