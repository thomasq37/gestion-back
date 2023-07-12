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
    
    @Autowired
    public AppartementService(
    		AppartementRepository appartementRepository, 
    		MouvementAppartementRepository mouvementAppartementRepository) 
    {
        this.appartementRepository = appartementRepository;
    }
    
    public List<Appartement> obtenirTousLesAppartements() {
        return appartementRepository.findAll();
    }
    
    public Appartement obtenirUnAppartementParId(Long id) throws IllegalArgumentException {
        Optional<Appartement> optionalAppartement = appartementRepository.findById(id);
        return optionalAppartement.orElseThrow(() -> new IllegalArgumentException("Appartement not found with ID: " + id));
    }
    
    public double calculerRentabiliteNette(Long id) {
        
            Appartement appartement = findById(id);

            double depensesTotales = 0.0;
            for (Frais frais : appartement.getFrais()) {
                double montantAnnuelEquivalent = frais.getFrequence().convertirMontantAnnuel(frais.getMontant());
                depensesTotales += montantAnnuelEquivalent;
            }

            double rentabiliteNette = ((appartement.getLoyerMensuel() * 12 - depensesTotales) / appartement.getPrix()) * 100;
            return rentabiliteNette;
        
    }

    public int calculerMoyenneBenefices(Long id){
            Appartement appartement = findById(id);

            double depensesTotales = 0.0;
            for (Frais frais : appartement.getFrais()) {
                double montantAnnuelEquivalent = frais.getFrequence().convertirMontantAnnuel(frais.getMontant());
                depensesTotales += montantAnnuelEquivalent;
            }

            int moyenneBenefices = (int) ((appartement.getLoyerMensuel() * 12 - depensesTotales) / 12);
            return moyenneBenefices;
        
    }

	public double calculerTauxVacancesLocatives(Long appartementId) {
	    Appartement appartement = findById(appartementId);
	
	    List<MouvementAppartement> mouvements = appartement.getMouvements();
	
	    mouvements.sort(Comparator.comparing(MouvementAppartement::getDate));
	
	    long totalJours = 0;
	    long joursVacances = 0;
	
	    for (int i = 0; i < mouvements.size() - 1; i++) {
	        MouvementAppartement courant = mouvements.get(i);
	        MouvementAppartement suivant = mouvements.get(i+1);
	
	        long joursEntre = ChronoUnit.DAYS.between(courant.getDate(), suivant.getDate());
	        totalJours += joursEntre;
	
	        if (!courant.isEstEntree()) {
	            joursVacances += joursEntre;
	        }
	    }
	
	    if (totalJours == 0) return 0.0;
	    double tauxVacances = (double) joursVacances / totalJours * 100;
	    return Math.round(tauxVacances * 100.0) / 100.0;
	}
}