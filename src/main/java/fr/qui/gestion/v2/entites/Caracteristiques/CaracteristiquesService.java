package fr.qui.gestion.v2.entites.Caracteristiques;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.Logement.LogementRepository;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CaracteristiquesService {

    private final CaracteristiquesRepository caracteristiquesRepository;
    private final LogementRepository logementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CaracteristiquesMapper caracteristiquesMapper;

    public CaracteristiquesService(
            CaracteristiquesRepository caracteristiquesRepository,
            LogementRepository logementRepository,
            UtilisateurRepository utilisateurRepository,
            CaracteristiquesMapper caracteristiquesMapper) {
        this.caracteristiquesRepository = caracteristiquesRepository;
        this.logementRepository = logementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.caracteristiquesMapper = caracteristiquesMapper;
    }

    @Transactional
    public CaracteristiquesDTO creerCaracteristiquesPourLogement(String logementMasqueId, Caracteristiques caracteristiques) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        if (logement.getCaracteristiques() != null) {
            throw new IllegalStateException("Des caracteristiques existe déjà pour ce logement. Veuillez les modifier.");
        }
        caracteristiques.setLogement(logement);
        Caracteristiques savedCaracteristiques = caracteristiquesRepository.save(caracteristiques);
        logement.setCaracteristiques(savedCaracteristiques);
        logementRepository.save(logement);
        return caracteristiquesMapper.toDto(savedCaracteristiques);
    }

    @Transactional(readOnly = true)
    public CaracteristiquesDTO obtenirCaracteristiquesPourLogement(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Caracteristiques caracteristiques = logement.getCaracteristiques();
        if (caracteristiques == null) {
            throw new IllegalArgumentException("Aucunes caracteristiques trouvées pour le logement.");
        }
        return caracteristiquesMapper.toDto(caracteristiques);
    }

    @Transactional
    public CaracteristiquesDTO modifierCaracteristiquesPourLogement(String logementMasqueId, Caracteristiques caracteristiquesModifiee) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Caracteristiques caracteristiques = logement.getCaracteristiques();
        if (caracteristiques == null) {
            throw new IllegalArgumentException("Aucunes caracteristiques à modifier pour le logement.");
        }

        caracteristiques.setDateAchat(caracteristiquesModifiee.getDateAchat());
        caracteristiques.setMontantAchat(caracteristiquesModifiee.getMontantAchat());
        caracteristiques.setMontantEstimation(caracteristiquesModifiee.getMontantEstimation());
        caracteristiques.setMontantFraisDeNotaireEtNegociation(caracteristiquesModifiee.getMontantFraisDeNotaireEtNegociation());
        caracteristiques.setNombreDePieces(caracteristiquesModifiee.getNombreDePieces());
        caracteristiques.setSurfaceLogement(caracteristiquesModifiee.getSurfaceLogement());
        caracteristiques.setTypeDeLogement(caracteristiquesModifiee.getTypeDeLogement());
        caracteristiques.setBalconOuTerrasse(caracteristiquesModifiee.getBalconOuTerrasse());
        caracteristiques.setSurfaceBalconOuTerrasse(caracteristiquesModifiee.getSurfaceBalconOuTerrasse());
        caracteristiques.setDpeLettre(caracteristiquesModifiee.getDpeLettre());
        caracteristiques.setDpeFichier(caracteristiquesModifiee.getDpeFichier());

        Caracteristiques savedCaracteristiques = caracteristiquesRepository.save(caracteristiques);
        return caracteristiquesMapper.toDto(savedCaracteristiques);
    }

    @Transactional
    public SuccessResponse supprimerCaracteristiquesPourLogement(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Caracteristiques caracteristiques = logement.getCaracteristiques();
        if (caracteristiques == null) {
            throw new IllegalArgumentException("Aucunes caracteristiques à supprimer pour le logement.");
        }

        logement.setCaracteristiques(null);
        logementRepository.save(logement);
        caracteristiquesRepository.delete(caracteristiques);
        return new SuccessResponse("Les caracteristiques ont été supprimé avec succès.");

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
