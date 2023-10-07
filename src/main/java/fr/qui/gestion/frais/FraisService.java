package fr.qui.gestion.frais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.appart.AppartementRepository;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.periodlocation.PeriodLocationRepository;

@Service
public class FraisService {
	@Autowired
    private FraisRepository fraisRepository;
	
	@Autowired
	private AppartementRepository appartementRepository;
	
	@Autowired
	private PeriodLocationRepository periodLocationRepository;

    public List<Frais> obtenirFraisFixesPourAppartement(Long appartId) {
        return fraisRepository.findByAppartementId(appartId);
    }
    
    public List<Frais> obtenirFraisFixesPourPeriode(Long periodeId) {
        return fraisRepository.findByPeriodLocationId(periodeId);
    }

	public Frais ajouterUnFraisPourAppartement(Long appartId, Frais newFrais) {
	    Appartement appartement = appartementRepository.findById(appartId)
	            .orElseThrow(() -> new RuntimeException("Appartement not found with id: " + appartId));
	    
	    newFrais.setAppartement(appartement);
	    return fraisRepository.save(newFrais);
	}

	public Frais ajouterUnFraisPourPeriode(Long appartId, Long periodeId, Frais newFrais) {

	    PeriodLocation periodLocation = periodLocationRepository.findById(periodeId)
	            .orElseThrow(() -> new RuntimeException("PeriodLocation not found with id: " + periodeId));

	    if (!periodLocation.getAppartement().getId().equals(appartId)) {
	        throw new RuntimeException("The specified period does not belong to the given apartment");
	    }

	    newFrais.setPeriodLocation(periodLocation);
	    return fraisRepository.save(newFrais);
	}

	
	public void supprimerFraisPourAppartement(Long fraisId) {
		fraisRepository.deleteById(fraisId);
	}
	
    public Frais mettreAJourUnFraisPourAppartement(Long appartId, Long fraisId, Frais fraisMisAJour) {
    	
        Frais fraisActuel = fraisRepository.findById(fraisId)
                .orElseThrow(() -> new RuntimeException("Frais non trouvé"));
        if (!fraisActuel.getAppartement().getId().equals(appartId)) {
            throw new RuntimeException("Le frais n'appartient pas à l'appartement donné");
        }
        fraisActuel.setMontant(fraisMisAJour.getMontant());
        fraisActuel.setFrequence(fraisMisAJour.getFrequence());
        fraisActuel.setTypeFrais(fraisMisAJour.getTypeFrais());
        return fraisRepository.save(fraisActuel);
    }

    public Frais mettreAJourUnFraisPourPeriode(Long appartId, Long periodeId, Long fraisId, Frais fraisMisAJour) {
        Frais fraisActuel = fraisRepository.findById(fraisId)
                .orElseThrow(() -> new RuntimeException("Frais non trouvé"));
        if (!fraisActuel.getAppartement().getId().equals(appartId)) {
            throw new RuntimeException("Le frais n'appartient pas à l'appartement donné");
        }
        if (fraisActuel.getPeriodLocation() == null || !fraisActuel.getPeriodLocation().getId().equals(periodeId)) {
            throw new RuntimeException("Le frais n'appartient pas à la période donnée");
        }
        fraisActuel.setMontant(fraisMisAJour.getMontant());
        fraisActuel.setFrequence(fraisMisAJour.getFrequence());
        fraisActuel.setTypeFrais(fraisMisAJour.getTypeFrais());
        return fraisRepository.save(fraisActuel);
    }


}
