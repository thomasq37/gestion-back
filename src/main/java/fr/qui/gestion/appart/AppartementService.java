package fr.qui.gestion.appart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.frais.FraisRepository;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.periodlocation.PeriodLocationRepository;
import jakarta.transaction.Transactional;

@Service
public class AppartementService {
    

    
    @Autowired
    private FraisRepository fraisRepository;
    
    @Autowired
    private PeriodLocationRepository periodLocationRepository;
    
    @Autowired
    private AppartementRepository appartementRepository;
    
    public List<Appartement> obtenirTousLesAppartements() {
        return appartementRepository.findAll();
    }
    
    public Appartement ajouterAppartement(Appartement nouvelAppartement) {
        return appartementRepository.save(nouvelAppartement);
    }
    
    public Appartement mettreAJourUnAppartementPourUtilisateur(Long userId, Long appartId, Appartement appartementModifie) {
        Appartement appartementExist = obtenirUnAppartementParId(appartId);
        if (appartementExist.getAppUser().getId() != userId) {
            throw new SecurityException("L'utilisateur n'est pas autorisé à mettre à jour cet appartement.");
        }
        appartementExist.setNumero(appartementModifie.getNumero());
        appartementExist.setAdresse(appartementModifie.getAdresse());
        appartementExist.setCodePostal(appartementModifie.getCodePostal());
        appartementExist.setVille(appartementModifie.getVille());
        appartementExist.setNombrePieces(appartementModifie.getNombrePieces());
        appartementExist.setSurface(appartementModifie.getSurface());
        appartementExist.setBalcon(appartementModifie.isBalcon());
        appartementExist.setPrix(appartementModifie.getPrix());
        appartementExist.setImages(appartementModifie.getImages());
        return appartementRepository.save(appartementExist);
    }

    public Appartement obtenirUnAppartementParId(Long id) throws IllegalArgumentException {
        Optional<Appartement> optionalAppartement = appartementRepository.findById(id);
        return optionalAppartement.orElseThrow(() -> new IllegalArgumentException("Appartement not found with ID: " + id));
    }
    
    public void supprimerUnAppartement(Long id) {
    	appartementRepository.deleteById(id);
    }
    
    // Frais
    
	@Transactional
	public void supprimerTousLesFraisParAppartementId(Long appartementId) {
       fraisRepository.deleteAllByAppartementId(appartementId);
   }
	
	@Transactional
	public Frais ajouterUnFraisPourPeriode(Long periodeId, Frais newFrais) {
		PeriodLocation periodLocation = periodLocationRepository.findById(periodeId)
				.orElseThrow(() -> new RuntimeException("PeriodLocation not found"));

        newFrais.setPeriodLocation(periodLocation);
        return fraisRepository.save(newFrais);
	}

	
	public Frais mettreAJourUnFraisPourPeriode(Long periodeId, Long fraisId, Frais fraisMisAJour) {
		Frais fraisActuel = fraisRepository.findById(fraisId)
              .orElseThrow(() -> new RuntimeException("Frais non trouvé"));
        if (!fraisActuel.getPeriodLocation().getId().equals(periodeId)) {
            throw new RuntimeException("Le frais n'appartient pas à la période donnée");
        }
        fraisActuel.setMontant(fraisMisAJour.getMontant());
        fraisActuel.setFrequence(fraisMisAJour.getFrequence());
        fraisActuel.setTypeFrais(fraisMisAJour.getTypeFrais());
        return fraisRepository.save(fraisActuel);
	}

	public void supprimerFraisPourPeriode(Long periodeId, Long fraisId) {
		Frais frais = fraisRepository.findById(fraisId)
                .orElseThrow(() -> new RuntimeException("Frais non trouvé"));

        if (!frais.getPeriodLocation().getId().equals(periodeId)) {
            throw new RuntimeException("Le frais n'appartient pas à la période donnée");
        }
        fraisRepository.delete(frais);
		
	}
	

}