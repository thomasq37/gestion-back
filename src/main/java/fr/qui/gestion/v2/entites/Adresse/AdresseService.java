package fr.qui.gestion.v2.entites.Adresse;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.Logement.LogementRepository;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdresseService {

    private final AdresseRepository adresseRepository;
    private final LogementRepository logementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AdresseMapper adresseMapper;

    public AdresseService(
            AdresseRepository adresseRepository,
            LogementRepository logementRepository,
            UtilisateurRepository utilisateurRepository,
            AdresseMapper adresseMapper) {
        this.adresseRepository = adresseRepository;
        this.logementRepository = logementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.adresseMapper = adresseMapper;
    }

    @Transactional
    public AdresseDTO creerAdressePourLogement(String logementMasqueId, AdresseDTO adresseDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        if (logement.getAdresse() != null) {
            throw new IllegalStateException("Une adresse existe déjà pour ce logement. Veuillez la modifier.");
        }
        if (adresseDTO.getNumero() == null || adresseDTO.getNumero().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de rue est obligatoire.");
        }
        if (adresseDTO.getVoie() == null || adresseDTO.getVoie().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la voie est obligatoire.");
        }
        if (adresseDTO.getCodePostal() == null || !adresseDTO.getCodePostal().matches("\\d{5}")) {
            throw new IllegalArgumentException("Le code postal est invalide. Il doit comporter 5 chiffres.");
        }
        if (adresseDTO.getVille() == null || adresseDTO.getVille().isEmpty()) {
            throw new IllegalArgumentException("La ville est obligatoire.");
        }
        if (adresseDTO.getPays() == null) {
            throw new IllegalArgumentException("Le pays est obligatoire.");
        }
        Adresse adresse = new Adresse();
        adresse.setNumero(adresseDTO.getNumero());
        adresse.setVoie(adresseDTO.getVoie());
        adresse.setComplementAdresse(adresseDTO.getComplementAdresse());
        adresse.setCodePostal(adresseDTO.getCodePostal());
        adresse.setVille(adresseDTO.getVille());
        adresse.setPays(adresseDTO.getPays());
        adresse.setLogement(logement);
        Adresse savedAdresse = adresseRepository.save(adresse);
        logement.setAdresse(savedAdresse);
        logementRepository.save(logement);
        return adresseMapper.toDto(savedAdresse);
    }

    @Transactional(readOnly = true)
    public AdresseDTO obtenirAdressePourLogement(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Adresse adresse = logement.getAdresse();
        if (adresse == null) {
            throw new IllegalArgumentException("Aucune adresse trouvée pour le logement.");
        }
        return adresseMapper.toDto(adresse);
    }

    @Transactional
    public AdresseDTO modifierAdressePourLogement(String logementMasqueId, AdresseDTO adresseModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Adresse adresse = logement.getAdresse();
        if (adresse == null) {
            throw new IllegalArgumentException("Aucune adresse à modifier pour le logement.");
        }
        if (adresseModifieeDTO.getNumero() == null || adresseModifieeDTO.getNumero().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de rue est obligatoire.");
        }
        if (adresseModifieeDTO.getVoie() == null || adresseModifieeDTO.getVoie().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la voie est obligatoire.");
        }
        if (adresseModifieeDTO.getCodePostal() == null || !adresseModifieeDTO.getCodePostal().matches("\\d{5}")) {
            throw new IllegalArgumentException("Le code postal est invalide. Il doit comporter 5 chiffres.");
        }
        if (adresseModifieeDTO.getVille() == null || adresseModifieeDTO.getVille().isEmpty()) {
            throw new IllegalArgumentException("La ville est obligatoire.");
        }
        if (adresseModifieeDTO.getPays() == null) {
            throw new IllegalArgumentException("Le pays est obligatoire.");
        }
        adresse.setNumero(adresseModifieeDTO.getNumero());
        adresse.setVoie(adresseModifieeDTO.getVoie());
        adresse.setComplementAdresse(adresseModifieeDTO.getComplementAdresse());
        adresse.setCodePostal(adresseModifieeDTO.getCodePostal());
        adresse.setVille(adresseModifieeDTO.getVille());
        adresse.setPays(adresseModifieeDTO.getPays());

        Adresse savedAdresse = adresseRepository.save(adresse);
        return adresseMapper.toDto(savedAdresse);
    }

    @Transactional
    public SuccessResponse supprimerAdressePourLogement(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Adresse adresse = logement.getAdresse();
        if (adresse == null) {
            throw new IllegalArgumentException("Aucune adresse à supprimer pour le logement.");
        }

        logement.setAdresse(null);
        logementRepository.save(logement);
        adresseRepository.delete(adresse);
        return new SuccessResponse("L'adresse a été supprimé avec succès.");

    }

    private Logement validerLogementPourUtilisateur(String logementMasqueId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Accès interdit ou utilisateur introuvable."));
        Logement logement = logementRepository.findByMasqueId(logementMasqueId)
                .orElseThrow(() -> new SecurityException("Accès interdit ou logement introuvable."));
        if (!logement.getProprietaire().equals(utilisateur)) {
            throw new SecurityException("Accès interdit au logement.");
        }
        return logement;
    }
}
