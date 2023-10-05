package fr.qui.gestion.contact;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.appart.AppartementRepository;

@Service
public class ContactService {
	@Autowired
    private ContactRepository contactRepository;
	
	@Autowired
	private AppartementRepository appartementRepository;

    public List<Contact> obtenirContactsParAppartement(Long appartId) {
        return contactRepository.findByAppartementId(appartId);
    }
    
    public Contact ajouterUnContactPourAppartement(Long appartId, Contact newContact) {
        Appartement appartement = appartementRepository.findById(appartId)
                .orElseThrow(() -> new RuntimeException("Appartement not found"));

        newContact.setAppartement(appartement);
        return contactRepository.save(newContact);
    }

    public void supprimerUnContactPourAppartement(Long fraisId) {
		contactRepository.deleteById(fraisId);
	}
    
	public Contact mettreAJourUnContactPourAppartement(Long appartId, Long contactId, Contact contactMisAJour) {
		Contact contactActuel = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact non trouvé"));
        if (!contactActuel.getAppartement().getId().equals(appartId)) {
            throw new RuntimeException("Le contact n'appartient pas à l'appartement donné");
        }
        contactActuel.setPseudo(contactMisAJour.getPseudo());
        contactActuel.setEmail(contactMisAJour.getEmail());
        contactActuel.setPhoneNumber(contactMisAJour.getPhoneNumber());
        return contactRepository.save(contactActuel);
	}
}
