package fr.qui.gestion.v2.entites.Locataire;
import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.Logement.LogementRepository;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocationRepository;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocataireService {

    private final LocataireRepository locataireRepository;
    private final LogementRepository logementRepository;
    private final PeriodeDeLocationRepository periodeDeLocationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final LocataireMapper locataireMapper;

    public LocataireService(
            LocataireRepository locataireRepository,
            LogementRepository logementRepository,
            PeriodeDeLocationRepository periodeDeLocationRepository,
            UtilisateurRepository utilisateurRepository,
            LocataireMapper locataireMapper) {
        this.locataireRepository = locataireRepository;
        this.logementRepository = logementRepository;
        this.periodeDeLocationRepository = periodeDeLocationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.locataireMapper = locataireMapper;
    }
    public List<LocataireDTO> listerLocatairePourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId) {
        // Valider que le logement appartient bien à l'utilisateur connecté
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        // Vérifier si la période est bien associée au logement
        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));
        // Récupérer les locataires associés à la période
        List<Locataire> locataires = locataireRepository.findByPeriodeDeLocation(periodeDeLocation);
        return locataires.stream()
                .map(locataireMapper::toDto)
                .toList();
    }
    @Transactional
    public LocataireDTO creerLocatairePourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId, LocataireDTO locataireDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));
        if (locataireDTO.getNom() == null || locataireDTO.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du locataire est obligatoire.");
        }
        if (locataireDTO.getPrenom() == null || locataireDTO.getPrenom().isEmpty()) {
            throw new IllegalArgumentException("Le prénom du locataire est obligatoire.");
        }
        Locataire locataire = new Locataire();
        locataire.setNom(locataireDTO.getNom());
        locataire.setPrenom(locataireDTO.getPrenom());
        locataire.setEmail(locataireDTO.getEmail());
        locataire.setTelephone(locataireDTO.getTelephone());

        Locataire savedLocataire = locataireRepository.save(locataire);
        periodeDeLocation.getLocataires().add(savedLocataire);
        periodeDeLocationRepository.save(periodeDeLocation);
        return locataireMapper.toDto(savedLocataire);
    }
    @Transactional(readOnly = true)
    public LocataireDTO obtenirLocatairePourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId, String locataireMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));

        Locataire locataire = periodeDeLocation.getLocataires().stream()
                .filter(c -> c.getMasqueId().equals(locataireMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou locataire introuvable."));

        return locataireMapper.toDto(locataire);
    }
    @Transactional
    public LocataireDTO modifierLocatairePourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId, String locataireMasqueId, LocataireDTO locataireModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));

        Locataire locataire = periodeDeLocation.getLocataires().stream()
                .filter(c -> c.getMasqueId().equals(locataireMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou frais introuvable."));
        if (locataireModifieeDTO.getNom() == null || locataireModifieeDTO.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du locataire est obligatoire.");
        }
        if (locataireModifieeDTO.getPrenom() == null || locataireModifieeDTO.getPrenom().isEmpty()) {
            throw new IllegalArgumentException("Le prénom du locataire est obligatoire.");
        }
        locataire.setNom(locataireModifieeDTO.getNom());
        locataire.setPrenom(locataireModifieeDTO.getPrenom());
        locataire.setEmail(locataireModifieeDTO.getEmail());
        locataire.setTelephone(locataireModifieeDTO.getTelephone());
        Locataire savedLocataire = locataireRepository.save(locataire);
        return locataireMapper.toDto(savedLocataire);
    }
    @Transactional
    public SuccessResponse supprimerLocatairePourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId, String locataireMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));

        Locataire locataire = periodeDeLocation.getLocataires().stream()
                .filter(c -> c.getMasqueId().equals(locataireMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou locataire introuvable."));

        periodeDeLocation.getLocataires().remove(locataire);
        periodeDeLocationRepository.save(periodeDeLocation);
        locataireRepository.delete(locataire);
        return new SuccessResponse("Le locataire a été supprimé avec succès.");
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
