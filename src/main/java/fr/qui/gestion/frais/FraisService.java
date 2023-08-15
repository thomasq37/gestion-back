package fr.qui.gestion.frais;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;

import java.util.List;

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
}