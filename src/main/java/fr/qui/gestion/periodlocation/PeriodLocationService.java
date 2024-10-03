package fr.qui.gestion.periodlocation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.appart.AppartementRepository;

@Service
public class PeriodLocationService {
	
	@Autowired
	private PeriodLocationRepository periodLocationRepository;
	
	@Autowired
	private AppartementRepository appartementRepository;

	public Page<PeriodLocation> obtenirPeriodeLocationParAppartement(Long appartId, Pageable pageable) {
	    Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "estEntree"));
	    return periodLocationRepository.findByAppartementId(appartId, pageableWithSort);
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
		periodeActuel.setLocataire(periodeMisAJour.getLocataire());
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
