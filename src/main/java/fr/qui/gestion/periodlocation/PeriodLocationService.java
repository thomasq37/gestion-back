package fr.qui.gestion.periodlocation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.appart.AppartementRepository;

@Service
public class PeriodLocationService {
	
	@Autowired
	private PeriodLocationRepository periodLocationRepository;
	
	@Autowired
	private AppartementRepository appartementRepository;

    public List<PeriodLocation> obtenirPeriodeLocationParAppartement(Long appartId) {
        return periodLocationRepository.findByAppartementId(appartId);
    }

	public PeriodLocation ajouterUnePeriodeLocationPourAppartement(Long appartId, PeriodLocation newPeriodLocation) {
		 Appartement appartement = appartementRepository.findById(appartId)
               .orElseThrow(() -> new RuntimeException("Appartement not found"));

		 newPeriodLocation.setAppartement(appartement);
       return periodLocationRepository.save(newPeriodLocation);
	}
    
	public PeriodLocation mettreAJourUnePeriodePourAppartement(Long appartId, Long periodeId, PeriodLocation periodeMisAJour) {
		PeriodLocation periodeActuel = periodLocationRepository.findById(periodeId)
                .orElseThrow(() -> new RuntimeException("Periode non trouvé"));
        if (!periodeActuel.getAppartement().getId().equals(appartId)) {
            throw new RuntimeException("La periode n'appartient pas à l'appartement donné");
        }
        periodeActuel.setPrix(periodeMisAJour.getPrix());
        periodeActuel.setEstEntree(periodeMisAJour.getEstEntree());
        periodeActuel.setEstSortie(periodeMisAJour.getEstSortie());
        periodeActuel.setLocVac(periodeMisAJour.isLocVac());
        return periodLocationRepository.save(periodeActuel);
	}

	public void supprimerUnePeriodePourAppartement(Long periodeId) {
		periodLocationRepository.deleteById(periodeId);
	}
}
