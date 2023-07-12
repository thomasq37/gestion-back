package fr.qui.gestion.appart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import fr.qui.gestion.frais.Frais;

import java.util.List;
import java.util.Optional;

@Service
public class AppartementService {
    private final AppartementRepository appartementRepository;

    @Autowired
    public AppartementService(AppartementRepository appartementRepository) {
        this.appartementRepository = appartementRepository;
    }
    public List<Appartement> obtenirTousLesAppartements() {
        return appartementRepository.findAll();
    }
    

    public Optional<Appartement> findById(Long id) {
        Optional<Appartement> optionalAppartement = appartementRepository.findById(id);
        return optionalAppartement;
    }
    
    public double calculerRentabiliteNette(Long id) throws ClassNotFoundException {
        Optional<Appartement> optionalAppartement = findById(id);

        if (optionalAppartement.isPresent()) {
            Appartement appartement = optionalAppartement.get();
            double depensesTotales = 0.0;
            for (Frais frais : appartement.getFrais()) {
                double montantAnnuelEquivalent = frais.getFrequence().convertirMontantAnnuel(frais.getMontant());
                depensesTotales += montantAnnuelEquivalent;
            }
            
            double rentabiliteNette = ((appartement.getLoyerMensuel() * 12 - depensesTotales) / appartement.getPrix()) * 100;
            return rentabiliteNette;
        } else {
            throw new ClassNotFoundException("Appartement non trouvé pour l'ID : " + id);
        }
    }
    
    public int calculerMoyenneBenefices(Long id) throws ClassNotFoundException {
        Optional<Appartement> optionalAppartement = findById(id);

        if (optionalAppartement.isPresent()) {
            Appartement appartement = optionalAppartement.get();
            double depensesTotales = 0.0;
            for (Frais frais : appartement.getFrais()) {
                double montantAnnuelEquivalent = frais.getFrequence().convertirMontantAnnuel(frais.getMontant());
                depensesTotales += montantAnnuelEquivalent;
            }
            
            int moyenneBenefices = (int) ((appartement.getLoyerMensuel() * 12 - depensesTotales) / 12);
            return moyenneBenefices;
        } else {
            throw new ClassNotFoundException("Appartement non trouvé pour l'ID : " + id);
        }
    }
}