package fr.qui.gestion.v2.entites.Alerte;

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
public class AlerteService {

    private final AlerteRepository alerteRepository;
    private final LogementRepository logementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AlerteMapper alerteMapper;

    public AlerteService(
            AlerteRepository alerteRepository,
            LogementRepository logementRepository,
            UtilisateurRepository utilisateurRepository,
            AlerteMapper alerteMapper) {
        this.alerteRepository = alerteRepository;
        this.logementRepository = logementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.alerteMapper = alerteMapper;
    }
    public List<AlerteDTO> listerAlertes(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        List<Alerte> alertes = alerteRepository.findByLogement(logement);
        return alertes.stream().map(alerteMapper::toDto).toList();
    }
    @Transactional
    public AlerteDTO creerAlertePourLogement(String logementMasqueId, AlerteDTO alerteDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        if (alerteDTO.getProbleme() == null || alerteDTO.getProbleme().isEmpty()) {
            throw new IllegalArgumentException("Le champ probleme est obligatoire.");
        }
        if (alerteDTO.getSolution() == null || alerteDTO.getSolution().isEmpty()) {
            throw new IllegalArgumentException("Le champ solution est obligatoire.");
        }
        Alerte alerte = new Alerte();
        alerte.setProbleme(alerteDTO.getProbleme());
        alerte.setSolution(alerteDTO.getSolution());
        alerte.setLogement(logement);
        Alerte savedAlerte = alerteRepository.save(alerte);
        logement.getAlertes().add(savedAlerte);
        logementRepository.save(logement);
        return alerteMapper.toDto(savedAlerte);
    }
    @Transactional(readOnly = true)
    public AlerteDTO obtenirAlertePourLogement(String logementMasqueId, String alerteMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Alerte alerte = logement.getAlertes().stream()
                .filter(c -> c.getMasqueId().equals(alerteMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou alerte introuvable."));
        return alerteMapper.toDto(alerte);
    }
    @Transactional
    public AlerteDTO modifierAlertePourLogement(String logementMasqueId, String alerteMasqueId, AlerteDTO alerteModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Alerte alerte = logement.getAlertes().stream()
                .filter(c -> c.getMasqueId().equals(alerteMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou alerte introuvable."));
        if (alerteModifieeDTO.getProbleme() == null || alerteModifieeDTO.getProbleme().isEmpty()) {
            throw new IllegalArgumentException("Le champ probleme est obligatoire.");
        }
        if (alerteModifieeDTO.getSolution() == null || alerteModifieeDTO.getSolution().isEmpty()) {
            throw new IllegalArgumentException("Le champ solution est obligatoire.");
        }
        alerte.setProbleme(alerteModifieeDTO.getProbleme());
        alerte.setSolution(alerteModifieeDTO.getSolution());
        Alerte savedAlerte = alerteRepository.save(alerte);
        return alerteMapper.toDto(savedAlerte);
    }
    @Transactional
    public SuccessResponse supprimerAlertePourLogement(String logementMasqueId, String alerteMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Alerte alerte = logement.getAlertes().stream()
                .filter(c -> c.getMasqueId().equals(alerteMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou alerte introuvable."));
        logement.getAlertes().remove(alerte);
        logementRepository.save(logement);
        alerteRepository.delete(alerte);
        return new SuccessResponse("L'alerte a été supprimé avec succès.");

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
