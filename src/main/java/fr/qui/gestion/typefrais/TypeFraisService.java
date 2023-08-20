package fr.qui.gestion.typefrais;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
}