package fr.qui.gestion.v2.entites.Logement;
import fr.qui.gestion.v2.entites.Adresse.Adresse;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogementService {

    private final LogementRepository logementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final LogementMapper logementMapper;

    public LogementService(
            LogementRepository logementRepository,
            UtilisateurRepository utilisateurRepository,
            LogementMapper logementMapper) {
        this.logementRepository = logementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.logementMapper = logementMapper;
    }

    public LogementDTO creerLogement() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        Logement nouveauLogement = new Logement();
        nouveauLogement.setProprietaire(utilisateur);
        nouveauLogement.setAdresse(null);
        nouveauLogement.setCaracteristiques(null);
        nouveauLogement.setContacts(new ArrayList<>());
        nouveauLogement.setFrais(new ArrayList<>());
        nouveauLogement.setPeriodesDeLocation(new ArrayList<>());
        nouveauLogement.setPhotos(new ArrayList<>());
        nouveauLogement.setGestionnaires(new ArrayList<>());
        logementRepository.save(nouveauLogement);
        return logementMapper.toDto(nouveauLogement);
    }
    public List<LogementDTO> listerLogements() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        List<Logement> logements = logementRepository.findByProprietaire(utilisateur);
        return logements.stream().map(logementMapper::toDto).toList();
    }

    public LogementDTO obtenirLogement(String logementMasqueId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        Logement logement = logementRepository.findByMasqueId(logementMasqueId)
                .orElseThrow(() -> new SecurityException("Acces interdit ou logement introuvable."));
        if (!logement.getProprietaire().equals(utilisateur)) {
            throw new SecurityException("Acces interdit ou logement introuvable.");
        }
        return logementMapper.toDto(logement);
    }
    public SuccessResponse supprimerLogement(String logementMasqueId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        Logement logement = logementRepository.findByMasqueId(logementMasqueId)
                .orElseThrow(() -> new SecurityException("Acces interdit ou logement introuvable."));
        if (!logement.getProprietaire().equals(utilisateur)) {
            throw new SecurityException("Acces interdit ou logement introuvable.");
        }
        logementRepository.delete(logement);
        return new SuccessResponse("Le logement a été supprimé avec succès.");
    }
}
