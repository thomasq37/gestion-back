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
    public AdresseDTO creerAdressePourLogement(String logementMasqueId, Adresse adresse) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        if (logement.getAdresse() != null) {
            throw new IllegalStateException("Une adresse existe déjà pour ce logement. Veuillez la modifier.");
        }
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
    public AdresseDTO modifierAdressePourLogement(String logementMasqueId, Adresse adresseModifiee) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Adresse adresse = logement.getAdresse();
        if (adresse == null) {
            throw new IllegalArgumentException("Aucune adresse à modifier pour le logement.");
        }
        adresse.setNumero(adresseModifiee.getNumero());
        adresse.setVoie(adresseModifiee.getVoie());
        adresse.setComplementAdresse(adresseModifiee.getComplementAdresse());
        adresse.setCodePostal(adresseModifiee.getCodePostal());
        adresse.setVille(adresseModifiee.getVille());
        adresse.setPays(adresseModifiee.getPays());

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
