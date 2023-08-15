package fr.qui.gestion.typefrais;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;

import java.util.List;

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
    
}