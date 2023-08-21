package fr.qui.gestion.typefrais;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.frais.Frais;

@Service
public class TypeFraisService {
    private final TypeFraisRepository typeFraisRepository;

    @Autowired
    public TypeFraisService(TypeFraisRepository TypeFraisRepository) {
        this.typeFraisRepository = TypeFraisRepository;
    }
    
    public List<TypeFrais> obtenirTousLesTypeDeFrais() {
        return typeFraisRepository.findAll();
    }
    
	public TypeFrais obtenirUnTypeDeFraisParId(Long id) throws IllegalArgumentException {
        Optional<TypeFrais> optionalTypeDeFrais = typeFraisRepository.findById(id);
        return optionalTypeDeFrais.orElseThrow(() -> new IllegalArgumentException("Type de Frais not found with ID: " + id));
    }
	
	public TypeFrais modifierTypeFrais(Long id, TypeFrais typeFraisModifie) {
		TypeFrais typeFraisExist = obtenirUnTypeDeFraisParId(id);

        if (typeFraisExist == null) {
            throw new IllegalArgumentException("Type de frais with ID " + id + " not found");
        }

        typeFraisExist.setNom(typeFraisModifie.getNom());
      
        return typeFraisRepository.save(typeFraisExist);
    }
	
	public TypeFrais ajouterTypeDeFrais(TypeFrais nouveauTypeDeFrais) {
		return typeFraisRepository.save(nouveauTypeDeFrais);
	}
	
	public void supprimerTypeDeFrais(Long id) {
		typeFraisRepository.deleteById(id);
    }
}