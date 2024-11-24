package fr.qui.gestion.v2.entites.PeriodeDeLocation;
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
public class PeriodeDeLocationService {

    private final PeriodeDeLocationRepository periodeDeLocationRepository;
    private final LogementRepository logementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PeriodeDeLocationMapper periodeDeLocationMapper;

    public PeriodeDeLocationService(
            PeriodeDeLocationRepository periodeDeLocationRepository,
            LogementRepository logementRepository,
            UtilisateurRepository utilisateurRepository,
            PeriodeDeLocationMapper periodeDeLocationMapper) {
        this.periodeDeLocationRepository = periodeDeLocationRepository;
        this.logementRepository = logementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.periodeDeLocationMapper = periodeDeLocationMapper;
    }
    public List<PeriodeDeLocationDTO> listerPeriodeDeLocations(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        List<PeriodeDeLocation> periodeDeLocations = periodeDeLocationRepository.findByLogement(logement);
        return periodeDeLocations.stream().map(periodeDeLocationMapper::toDto).toList();
    }
    @Transactional
    public PeriodeDeLocationDTO creerPeriodeDeLocationPourLogement(String logementMasqueId, PeriodeDeLocationDTO periodeDeLocationDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        if (periodeDeLocationDTO.getTarif() == null || periodeDeLocationDTO.getTarif() <= 0) {
            throw new IllegalArgumentException("Le tarif de la période de location est obligatoire et doit être supérieur à zéro.");
        }
        if (periodeDeLocationDTO.getDateDeDebut() == null) {
            throw new IllegalArgumentException("La date de début de la période de location est obligatoire.");
        }
        if (periodeDeLocationDTO.getDateDeFin() != null && periodeDeLocationDTO.getDateDeFin().isBefore(periodeDeLocationDTO.getDateDeDebut())) {
            throw new IllegalArgumentException("La date de fin de la période de location ne peut pas être antérieure à la date de début.");
        }
        if (periodeDeLocationDTO.getTypeDeLocation() == null) {
            throw new IllegalArgumentException("Le type de location est obligatoire.");
        }
        PeriodeDeLocation periodeDeLocation = new PeriodeDeLocation();
        periodeDeLocation.setTarif(periodeDeLocationDTO.getTarif());
        periodeDeLocation.setDateDeDebut(periodeDeLocationDTO.getDateDeDebut());
        periodeDeLocation.setDateDeFin(periodeDeLocationDTO.getDateDeFin());
        periodeDeLocation.setTypeDeLocation(periodeDeLocationDTO.getTypeDeLocation());
        periodeDeLocation.setLogement(logement);
        PeriodeDeLocation savedPeriodeDeLocation = periodeDeLocationRepository.save(periodeDeLocation);
        logement.getPeriodesDeLocation().add(savedPeriodeDeLocation);
        logementRepository.save(logement);
        return periodeDeLocationMapper.toDto(savedPeriodeDeLocation);
    }
    @Transactional(readOnly = true)
    public PeriodeDeLocationDTO obtenirPeriodeDeLocationPourLogement(String logementMasqueId, String periodeDeLocationMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(c -> c.getMasqueId().equals(periodeDeLocationMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou periode de location introuvable."));
        return periodeDeLocationMapper.toDto(periodeDeLocation);
    }
    @Transactional
    public PeriodeDeLocationDTO modifierPeriodeDeLocationPourLogement(String logementMasqueId, String periodeDeLocationMasqueId, PeriodeDeLocationDTO periodeDeLocationModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(c -> c.getMasqueId().equals(periodeDeLocationMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou periode de location introuvable."));
        if (periodeDeLocationModifieeDTO.getTarif() == null || periodeDeLocationModifieeDTO.getTarif() <= 0) {
            throw new IllegalArgumentException("Le tarif de la période de location est obligatoire et doit être supérieur à zéro.");
        }
        if (periodeDeLocationModifieeDTO.getDateDeDebut() == null) {
            throw new IllegalArgumentException("La date de début de la période de location est obligatoire.");
        }
        if (periodeDeLocationModifieeDTO.getDateDeFin() != null && periodeDeLocationModifieeDTO.getDateDeFin().isBefore(periodeDeLocation.getDateDeDebut())) {
            throw new IllegalArgumentException("La date de fin de la période de location ne peut pas être antérieure à la date de début.");
        }
        if (periodeDeLocationModifieeDTO.getTypeDeLocation() == null) {
            throw new IllegalArgumentException("Le type de location est obligatoire.");
        }
        periodeDeLocation.setTarif(periodeDeLocationModifieeDTO.getTarif());
        periodeDeLocation.setDateDeDebut(periodeDeLocationModifieeDTO.getDateDeDebut());
        periodeDeLocation.setDateDeFin(periodeDeLocationModifieeDTO.getDateDeFin());
        periodeDeLocation.setTypeDeLocation(periodeDeLocationModifieeDTO.getTypeDeLocation());
        PeriodeDeLocation savedPeriodeDeLocation = periodeDeLocationRepository.save(periodeDeLocation);
        return periodeDeLocationMapper.toDto(savedPeriodeDeLocation);
    }
    @Transactional
    public SuccessResponse supprimerPeriodeDeLocationPourLogement(String logementMasqueId, String periodeDeLocationMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(c -> c.getMasqueId().equals(periodeDeLocationMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou periode de location introuvable."));
        logement.getPeriodesDeLocation().remove(periodeDeLocation);
        logementRepository.save(logement);
        periodeDeLocationRepository.delete(periodeDeLocation);
        return new SuccessResponse("La periode de location a été supprimé avec succès.");

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
