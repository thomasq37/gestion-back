package fr.qui.gestion.v2.entites.Contact;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.Logement.LogementRepository;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final LogementRepository logementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ContactMapper contactMapper;

    public ContactService(
            ContactRepository contactRepository,
            LogementRepository logementRepository,
            UtilisateurRepository utilisateurRepository,
            ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.logementRepository = logementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.contactMapper = contactMapper;
    }
    public List<ContactDTO> listerContacts(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        List<Contact> contacts = contactRepository.findByLogement(logement);
        return contacts.stream().map(contactMapper::toDto).toList();
    }
    @Transactional
    public ContactDTO creerContactPourLogement(String logementMasqueId, Contact contact) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        if (contact.getNom() == null || contact.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du contact est obligatoire.");
        }
        if (contact.getPrenom() == null || contact.getPrenom().isEmpty()) {
            throw new IllegalArgumentException("Le prénom du contact est obligatoire.");
        }
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (contact.getEmail() != null && !contact.getEmail().matches(emailPattern)) {
            throw new IllegalArgumentException("L'email du contact n'est pas valide.");
        }
        if (contact.getTelephone() != null && !contact.getTelephone().matches("^(\\+\\d{1,3}[- ]?)?\\d{10}$")) {
            throw new IllegalArgumentException("Numéro de téléphone invalide.");
        }
        contact.setLogement(logement);
        Contact savedContact = contactRepository.save(contact);
        logement.getContacts().add(savedContact);
        logementRepository.save(logement);
        return contactMapper.toDto(savedContact);
    }
    @Transactional(readOnly = true)
    public ContactDTO obtenirContactPourLogement(String logementMasqueId, String contactMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Contact contact = logement.getContacts().stream()
                .filter(c -> c.getMasqueId().equals(contactMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou contact introuvable."));
        return contactMapper.toDto(contact);
    }
    @Transactional
    public ContactDTO modifierContactPourLogement(String logementMasqueId, String contactMasqueId, Contact contactModifiee) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Contact contact = logement.getContacts().stream()
                .filter(c -> c.getMasqueId().equals(contactMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou contact introuvable."));
        if (contactModifiee.getNom() == null || contactModifiee.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du contact est obligatoire.");
        }
        if (contactModifiee.getPrenom() == null || contactModifiee.getPrenom().isEmpty()) {
            throw new IllegalArgumentException("Le prénom du contact est obligatoire.");
        }
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (contactModifiee.getEmail() != null && !contactModifiee.getEmail().matches(emailPattern)) {
            throw new IllegalArgumentException("L'email du contact n'est pas valide.");
        }
        if (contactModifiee.getTelephone() != null && !contactModifiee.getTelephone().matches("^(\\+\\d{1,3}[- ]?)?\\d{10}$")) {
            throw new IllegalArgumentException("Numéro de téléphone du contact invalide.");
        }
        contact.setNom(contactModifiee.getNom());
        contact.setPrenom(contactModifiee.getPrenom());
        contact.setTelephone(contactModifiee.getTelephone());
        contact.setEmail(contactModifiee.getEmail());
        Contact savedContact = contactRepository.save(contact);
        return contactMapper.toDto(savedContact);
    }
    @Transactional
    public SuccessResponse supprimerContactPourLogement(String logementMasqueId, String contactMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Contact contact = logement.getContacts().stream()
                .filter(c -> c.getMasqueId().equals(contactMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou contact introuvable."));
        logement.getContacts().remove(contact);
        logementRepository.save(logement);
        contactRepository.delete(contact);
        return new SuccessResponse("Le contact a été supprimé avec succès.");

    }
    private Logement validerLogementPourUtilisateur(String logementMasqueId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Accès interdit ou utilisateur introuvable."));
        Logement logement = logementRepository.findByMasqueId(logementMasqueId)
                .orElseThrow(() -> new SecurityException("Accès interdit ou logement introuvable."));
        if (!logement.getProprietaire().equals(utilisateur)) {
            throw new SecurityException("Accès interdit ou logement introuvable.");
        }
        return logement;
    }
}
