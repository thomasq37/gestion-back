package fr.qui.gestion.frais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.appart.AppartementRepository;

@Service
public class FraisService {
	@Autowired
    private FraisRepository fraisRepository;
	
	@Autowired
	private AppartementRepository appartementRepository;

    public List<Frais> obtenirFraisFixesPourAppartement(Long appartId) {
        return fraisRepository.findByAppartementId(appartId);
    }

	public Frais ajouterUnFraisPourAppartement(Long appartId, Frais newFrais) {
	    Appartement appartement = appartementRepository.findById(appartId)
	            .orElseThrow(() -> new RuntimeException("Appartement not found with id: " + appartId));
	    
	    newFrais.setAppartement(appartement);
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
}
