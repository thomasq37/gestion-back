package fr.qui.gestion.finance.mouvement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MouvementService {

    @Autowired
    private MouvementRepository mouvementRepository;

    public List<Mouvement> findAll() {
        return mouvementRepository.findAll();
    }

    public Optional<Mouvement> findById(Long id) {
        return mouvementRepository.findById(id);
    }
    
    public Mouvement update(Long id, Mouvement mouvementDetails) {
        return mouvementRepository.findById(id)
                .map(existingMouvement -> {
                    existingMouvement.setType(mouvementDetails.getType());
                    existingMouvement.setNom(mouvementDetails.getNom());
                    existingMouvement.setMontant(mouvementDetails.getMontant());
                    existingMouvement.setFrequence(mouvementDetails.getFrequence());
                    return mouvementRepository.save(existingMouvement);
                })
                .orElseThrow(() -> new IllegalArgumentException("Mouvement not found with id " + id));
    }
    public Mouvement save(Mouvement mouvement) {
        return mouvementRepository.save(mouvement);
    }

    public void deleteById(Long id) {
        mouvementRepository.deleteById(id);
    }
    


}