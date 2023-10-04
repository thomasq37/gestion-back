package fr.qui.gestion.appart;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.contact.Contact;
import fr.qui.gestion.contact.ContactRepository;
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
    
    @Autowired
    private ContactRepository contactRepository;
    
    public List<Appartement> obtenirTousLesAppartements() {
        return appartementRepository.findAll();
    }
    
    public List<AdresseDTO> obtenirToutesLesAdressesAppartements(){
    	return appartementRepository.findAllAdresses();
    }
    
    public Appartement ajouterAppartement(Appartement nouvelAppartement) {
        return appartementRepository.save(nouvelAppartement);
    }
    
    public Appartement modifierAppartement(Long id, Appartement appartementModifie) {
        Appartement appartementExist = obtenirUnAppartementParId(id);

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
    public Frais mettreAJourUnFraisPourAppartement(Long appartementId, Long fraisId, Frais fraisMisAJour) {
    	
        Frais fraisActuel = fraisRepository.findById(fraisId)
                .orElseThrow(() -> new RuntimeException("Frais non trouvé"));
        if (!fraisActuel.getAppartement().getId().equals(appartementId)) {
            throw new RuntimeException("Le frais n'appartient pas à l'appartement donné");
        }
        fraisActuel.setMontant(fraisMisAJour.getMontant());
        fraisActuel.setFrequence(fraisMisAJour.getFrequence());
        fraisActuel.setTypeFrais(fraisMisAJour.getTypeFrais());
        return fraisRepository.save(fraisActuel);
    }
    
    @Transactional
    public Frais ajouterUnFraisPourAppartement(Long appartementId, Frais newFrais) {
        Appartement appartement = appartementRepository.findById(appartementId)
                .orElseThrow(() -> new RuntimeException("Appartement not found"));

        newFrais.setAppartement(appartement);
        return fraisRepository.save(newFrais);
    }
    
    @Transactional
    public void supprimerFraisPourAppartement(Long appartementId, Long fraisId) {
        Frais frais = fraisRepository.findById(fraisId)
                .orElseThrow(() -> new RuntimeException("Frais non trouvé"));

        if (!frais.getAppartement().getId().equals(appartementId)) {
            throw new RuntimeException("Le frais n'appartient pas à l'appartement donné");
        }
        fraisRepository.delete(frais);
    }

	@Transactional
	public void supprimerTousLesFraisParAppartementId(Long appartementId) {
       fraisRepository.deleteAllByAppartementId(appartementId);
   }

	
	public PeriodLocation mettreAJourUnePeriodePourAppartement(Long appartementId, Long periodLocationId,
			PeriodLocation periodLocationMisAJour) {
		PeriodLocation periodLocationActuel = periodLocationRepository.findById(periodLocationId)
                .orElseThrow(() -> new RuntimeException("periodLocation non trouvé"));
        if (!periodLocationActuel.getAppartement().getId().equals(appartementId)) {
            throw new RuntimeException("Le frais n'appartient pas à l'appartement donné");
        }
        periodLocationActuel.setEstEntree(periodLocationMisAJour.getEstEntree());
        periodLocationActuel.setEstSortie(periodLocationMisAJour.getEstSortie());
        periodLocationActuel.setLocVac(periodLocationMisAJour.isLocVac());
        periodLocationActuel.setPrix(periodLocationMisAJour.getPrix());
        periodLocationActuel.setFrais(periodLocationMisAJour.getFrais());
        return periodLocationRepository.save(periodLocationActuel);
	}

	public PeriodLocation ajouterUnePeriodePourAppartement(Long appartementId, PeriodLocation newPeriodLocation) {
		Appartement appartement = appartementRepository.findById(appartementId)
                .orElseThrow(() -> new RuntimeException("Appartement not found"));

		newPeriodLocation.setAppartement(appartement);
        return periodLocationRepository.save(newPeriodLocation);
	}

	@Transactional
	public void supprimerPeriodePourAppartement(Long appartementId, Long periodLocationId) {
		PeriodLocation periodLocation = periodLocationRepository.findById(periodLocationId)
	                .orElseThrow(() -> new RuntimeException("PeriodLocation non trouvé"));
	    if (!periodLocation.getAppartement().getId().equals(appartementId)) {
            throw new RuntimeException("La periode de location n'appartient pas à l'appartement donné");
        }
	    fraisRepository.deleteByPeriodLocationId(periodLocationId);
        periodLocationRepository.delete(periodLocation);
		
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
	
	// Contacts
	
	public List<Contact> obtenirContactsPourAppartement(Long appartementId) {
	    Appartement appartement = appartementRepository.findById(appartementId)
	        .orElseThrow(() -> new RuntimeException("Appartement non trouvé"));

	    return appartement.getContacts();
	}
	
    @Transactional
    public Contact ajouterUnContactPourAppartement(Long appartementId, Contact newContact) {
        Appartement appartement = appartementRepository.findById(appartementId)
                .orElseThrow(() -> new RuntimeException("Appartement not found"));

        newContact.setAppartement(appartement);
        return contactRepository.save(newContact);
    }

	public Contact mettreAJourUnContactPourAppartement(Long appartementId, Long contactId, Contact contactMisAJour) {
		Contact contactActuel = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact non trouvé"));
        if (!contactActuel.getAppartement().getId().equals(appartementId)) {
            throw new RuntimeException("Le contact n'appartient pas à l'appartement donné");
        }
        contactActuel.setPseudo(contactMisAJour.getPseudo());
        contactActuel.setEmail(contactMisAJour.getEmail());
        contactActuel.setPhoneNumber(contactMisAJour.getPhoneNumber());
        return contactRepository.save(contactActuel);
	}
}