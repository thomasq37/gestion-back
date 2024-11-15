package fr.qui.gestion.periodlocation;

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
@RequestMapping(path = "/api/appartements/{appartId}/periodes", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origin}")
public class PeriodLocationController {
	
	private final PeriodLocationService periodLocationService;
	    
    @Autowired
    public PeriodLocationController(PeriodLocationService periodLocationService) {
        this.periodLocationService = periodLocationService;
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

    // utilise v2 //
    @GetMapping()
    public ResponseEntity<Page<PeriodLocation>> obtenirPeriodeLocationParAppartement(@PathVariable Long appartId, Pageable pageable) {

        Page<PeriodLocation> periodLocations = periodLocationService.obtenirPeriodeLocationParAppartement(appartId, pageable);
        return ResponseEntity.ok(periodLocations);
    }
}