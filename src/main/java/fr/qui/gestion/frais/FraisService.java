package fr.qui.gestion.frais;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FraisService {
	private final FraisRepository fraisRepository;
	
	@Autowired
	public FraisService(FraisRepository FraisRepository) {
	    this.fraisRepository = FraisRepository;
	}
	
	public List<Frais> obtenirTousLesFrais() {
	    return fraisRepository.findAll();
	}
	
	public List<Frais> obtenirTousLesFraisParAppartementId(Long appartementId){
		return fraisRepository.findByAppartementId(appartementId);
	}
	
	public Frais ajouterFrais(Frais nouveauFrais) {
		return fraisRepository.save(nouveauFrais);
	}
	
	public Frais modifierFrais(Long id, Frais fraisModifie) {
        Frais fraisExist = obtenirUnFraisParId(id);

        if (fraisExist == null) {
            throw new IllegalArgumentException("Frais with ID " + id + " not found");
        }

        fraisExist.setMontant(fraisModifie.getMontant());
        fraisExist.setFrequence(fraisModifie.getFrequence());
        fraisExist.setTypeFrais(fraisModifie.getTypeFrais());

        return fraisRepository.save(fraisExist);
    }
	
	public Frais obtenirUnFraisParId(Long id) throws IllegalArgumentException {
        Optional<Frais> optionalFrais = fraisRepository.findById(id);
        return optionalFrais.orElseThrow(() -> new IllegalArgumentException("Frais not found with ID: " + id));
    }
	
	 public void supprimerUnFrais(Long id) {
        fraisRepository.deleteById(id);
    }
	 
}