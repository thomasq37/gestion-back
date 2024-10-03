package fr.qui.gestion.frais;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Frais> obtenirFraisFixesPourAppartement(Long appartId, Pageable pageable) {
        Sort sort = Sort.by(
                Sort.Order.by("datePaiement").nullsFirst(),
                Sort.Order.desc("datePaiement"));
	    Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return fraisRepository.findByAppartementId(appartId, pageableWithSort);
    }
    
    public Page<Frais> obtenirFraisFixesPourPeriode(Long periodeId, Pageable pageable) {
	    Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return fraisRepository.findByPeriodLocationId(periodeId, pageableWithSort);
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
        fraisActuel.setNom(fraisMisAJour.getNom());

        fraisActuel.setMontant(fraisMisAJour.getMontant());
        fraisActuel.setFrequence(fraisMisAJour.getFrequence());
        fraisActuel.setDatePaiement(fraisMisAJour.getDatePaiement());

        fraisActuel.setTypeFrais(fraisMisAJour.getTypeFrais());
        return fraisRepository.save(fraisActuel);
    }

    public Frais mettreAJourUnFraisPourPeriode(Long appartId, Long periodeId, Long fraisId, Frais fraisMisAJour) {
        Frais fraisActuel = fraisRepository.findById(fraisId)
                .orElseThrow(() -> new RuntimeException("Frais non trouvé"));

        if (fraisActuel.getPeriodLocation() == null || !fraisActuel.getPeriodLocation().getId().equals(periodeId)) {
            throw new RuntimeException("Le frais n'appartient pas à la période donnée");
        }
        if (!fraisActuel.getPeriodLocation().getAppartement().getId().equals(appartId)) {
            throw new RuntimeException("Le frais n'appartient pas à l'appartement donné");
        }
        fraisActuel.setMontant(fraisMisAJour.getMontant());
        fraisActuel.setFrequence(fraisMisAJour.getFrequence());
        fraisActuel.setTypeFrais(fraisMisAJour.getTypeFrais());
        return fraisRepository.save(fraisActuel);
    }


}
