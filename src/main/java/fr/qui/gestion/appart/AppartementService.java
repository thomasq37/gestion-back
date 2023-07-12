package fr.qui.gestion.appart;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.mouvementappart.MouvementAppartement;
import fr.qui.gestion.mouvementappart.MouvementAppartementRepository;

@Service
public class AppartementService {
    private final AppartementRepository appartementRepository;
    private final MouvementAppartementRepository mouvementAppartementRepository;
    
    @Autowired
    public AppartementService(
    		AppartementRepository appartementRepository, 
    		MouvementAppartementRepository mouvementAppartementRepository) 
    {
        this.appartementRepository = appartementRepository;
        this.mouvementAppartementRepository = mouvementAppartementRepository;
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

    public double calculerTauxVacancesLocatives(Long appartementId) {
        Appartement appartement = appartementRepository.findById(appartementId)
                .orElseThrow(() -> new IllegalArgumentException("Appartement non trouvé"));

        List<MouvementAppartement> mouvements = appartement.getMouvements();
        
        // Assurez-vous que la liste est triée par date
        mouvements.sort(Comparator.comparing(MouvementAppartement::getDate));
        
        // Initialize le total de jours et les jours de vacances à zéro
        long totalJours = 0;
        long joursVacances = 0;

        // Parcourir tous les mouvements
        for (int i = 0; i < mouvements.size() - 1; i++) {
            MouvementAppartement courant = mouvements.get(i);
            MouvementAppartement suivant = mouvements.get(i+1);

            long joursEntre = ChronoUnit.DAYS.between(courant.getDate(), suivant.getDate());
            totalJours += joursEntre;

            // Si le mouvement actuel est une sortie (c'est-à-dire l'appartement est vacant),
            // alors ajouter le nombre de jours entre les deux mouvements aux jours de vacances
            if (!courant.isEstEntree()) {
                joursVacances += joursEntre;
            }
        }

        if (totalJours == 0) return 0.0;
        double tauxVacances = (double) joursVacances / totalJours * 100;
        double roundedTauxVacances = Math.round(tauxVacances * 100.0) / 100.0;
        return roundedTauxVacances;
    }
}