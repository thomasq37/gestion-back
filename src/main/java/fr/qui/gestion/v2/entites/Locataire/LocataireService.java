package fr.qui.gestion.v2.entites.Locataire;
import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.Logement.LogementRepository;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocationDTO;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocationMapper;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocationRepository;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocataireService {

    private final LocataireRepository locataireRepository;
    private final LogementRepository logementRepository;
    private final PeriodeDeLocationRepository periodeDeLocationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final LocataireMapper locataireMapper;
    private final PeriodeDeLocationMapper periodeDeLocationMapper;

    public LocataireService(
            LocataireRepository locataireRepository,
            LogementRepository logementRepository,
            PeriodeDeLocationRepository periodeDeLocationRepository,
            UtilisateurRepository utilisateurRepository,
            LocataireMapper locataireMapper,
            PeriodeDeLocationMapper periodeDeLocationMapper) {
        this.locataireRepository = locataireRepository;
        this.logementRepository = logementRepository;
        this.periodeDeLocationRepository = periodeDeLocationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.locataireMapper = locataireMapper;
        this.periodeDeLocationMapper = periodeDeLocationMapper;
    }
    public List<LocataireDTO> listerLocatairesPourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));
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
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (locataireDTO.getEmail() != null && !locataireDTO.getEmail().isEmpty() && !locataireDTO.getEmail().matches(emailPattern)) {
            throw new IllegalArgumentException("L'email du contact n'est pas valide.");
        }
        if (locataireDTO.getTelephone() != null && !locataireDTO.getTelephone().isEmpty() && !locataireDTO.getTelephone().matches("^(\\+\\d{1,3}[- ]?)?\\d{10}$")) {
            throw new IllegalArgumentException("Numéro de téléphone invalide.");
        }
        Locataire locataire = new Locataire();
        locataire.setNom(locataireDTO.getNom());
        locataire.setPrenom(locataireDTO.getPrenom());
        locataire.setEmail(locataireDTO.getEmail());
        locataire.setTelephone(locataireDTO.getTelephone());
        locataire.setDateDeNaissance(locataireDTO.getDateDeNaissance());
        locataire.setPeriodeDeLocation(periodeDeLocation);
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
    @Transactional(readOnly = true)
    public PeriodeDeLocationDTO obtenirPeriodeDeLocationPourLocataire(String logementMasqueId, String locataireMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Optional<Locataire> optionalLocataire = locataireRepository.findByMasqueId(locataireMasqueId);
        if(optionalLocataire.isPresent()){
            Optional<PeriodeDeLocation> optionalPeriodeDeLocation = periodeDeLocationRepository.findByLocataires(optionalLocataire.get());
            if(optionalPeriodeDeLocation.isPresent()){
                return periodeDeLocationMapper.toDto(optionalPeriodeDeLocation.get());
            }
            else{
                throw new SecurityException("Acces interdit ou periode de location introuvable.");
            }
        }
        else{
            throw new SecurityException("Acces interdit ou locataire introuvable.");
        }
    }

    @Transactional
    public LocataireDTO modifierLocatairePourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId, String locataireMasqueId, LocataireDTO locataireModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));
        Locataire locataire = locataireRepository.findByMasqueId(locataireMasqueId)
                .orElseThrow(() -> new SecurityException("Acces interdit ou locataire introuvable."));
        if (locataireModifieeDTO.getNom() == null || locataireModifieeDTO.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du locataire est obligatoire.");
        }
        if (locataireModifieeDTO.getPrenom() == null || locataireModifieeDTO.getPrenom().isEmpty()) {
            throw new IllegalArgumentException("Le prénom du locataire est obligatoire.");
        }
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (locataireModifieeDTO.getEmail() != null && !locataireModifieeDTO.getEmail().isEmpty() && !locataireModifieeDTO.getEmail().matches(emailPattern)) {
            throw new IllegalArgumentException("L'email du contact n'est pas valide.");
        }
        if (locataireModifieeDTO.getTelephone() != null && !locataireModifieeDTO.getTelephone().isEmpty() && !locataireModifieeDTO.getTelephone().matches("^(\\+\\d{1,3}[- ]?)?\\d{10}$")) {
            throw new IllegalArgumentException("Numéro de téléphone invalide.");
        }
        locataire.setNom(locataireModifieeDTO.getNom());
        locataire.setPrenom(locataireModifieeDTO.getPrenom());
        locataire.setEmail(locataireModifieeDTO.getEmail());
        locataire.setTelephone(locataireModifieeDTO.getTelephone());
        locataire.setDateDeNaissance(locataireModifieeDTO.getDateDeNaissance());
        locataire.setPeriodeDeLocation(periodeDeLocation);
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
