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
    public CaracteristiquesDTO creerCaracteristiquesPourLogement(String logementMasqueId, CaracteristiquesDTO caracteristiquesDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        if (logement.getCaracteristiques() != null) {
            throw new IllegalStateException("Des caracteristiques existe déjà pour ce logement. Veuillez les modifier.");
        }
        if (caracteristiquesDTO.getDateAchat() == null) {
            throw new IllegalArgumentException("La date d'achat est obligatoire.");
        }
        if (caracteristiquesDTO.getMontantAchat() == null || caracteristiquesDTO.getMontantAchat() <= 0) {
            throw new IllegalArgumentException("Le montant d'achat est obligatoire et doit être supérieur à zéro.");
        }
        if (caracteristiquesDTO.getMontantEstimation() != null && caracteristiquesDTO.getMontantEstimation() <= 0) {
            throw new IllegalArgumentException("Le montant d'estimation, s'il est présent, doit être supérieur à zéro.");
        }
        if (caracteristiquesDTO.getMontantFraisDeNotaireEtNegociation() != null && caracteristiquesDTO.getMontantFraisDeNotaireEtNegociation() <= 0) {
            throw new IllegalArgumentException("Le montant des frais de notaire et négociations, s'il est présent, doit être supérieur à zéro.");
        }
        if (caracteristiquesDTO.getNombreDePieces() == null || caracteristiquesDTO.getNombreDePieces() <= 0) {
            throw new IllegalArgumentException("Le nombre de pièces est obligatoire et doit être supérieur à zéro.");
        }
        if (caracteristiquesDTO.getSurfaceLogement() == null || caracteristiquesDTO.getSurfaceLogement() <= 0) {
            throw new IllegalArgumentException("La surface du logement est obligatoire et doit être supérieure à zéro.");
        }
        if (caracteristiquesDTO.getTypeDeLogement() == null) {
            throw new IllegalArgumentException("Le type de logement est obligatoire.");
        }
        if (caracteristiquesDTO.getMeubleeOuNon() == null) {
            throw new IllegalArgumentException("Veuillez spécifier si le logement est meublé ou non.");
        }
        if (caracteristiquesDTO.getBalconOuTerrasse() != null && caracteristiquesDTO.getBalconOuTerrasse() && (caracteristiquesDTO.getSurfaceBalconOuTerrasse() == null || caracteristiquesDTO.getSurfaceBalconOuTerrasse() <= 0)) {
            throw new IllegalArgumentException("Si un balcon ou une terrasse est présent(e), la surface doit être supérieure à zéro.");
        }
        if (caracteristiquesDTO.getDpeLettre() == null) {
            throw new IllegalArgumentException("La lettre DPE est obligatoire.");
        }
        Caracteristiques caracteristiques = new Caracteristiques();
        caracteristiques.setDateAchat(caracteristiquesDTO.getDateAchat());
        caracteristiques.setMontantAchat(caracteristiquesDTO.getMontantAchat());
        caracteristiques.setMontantEstimation(caracteristiquesDTO.getMontantEstimation());
        caracteristiques.setMontantFraisDeNotaireEtNegociation(caracteristiquesDTO.getMontantFraisDeNotaireEtNegociation());
        caracteristiques.setNombreDePieces(caracteristiquesDTO.getNombreDePieces());
        caracteristiques.setSurfaceLogement(caracteristiquesDTO.getSurfaceLogement());
        caracteristiques.setTypeDeLogement(caracteristiquesDTO.getTypeDeLogement());
        caracteristiques.setMeubleeOuNon(caracteristiquesDTO.getMeubleeOuNon());
        caracteristiques.setBalconOuTerrasse(caracteristiquesDTO.getBalconOuTerrasse());
        caracteristiques.setSurfaceBalconOuTerrasse(caracteristiquesDTO.getSurfaceBalconOuTerrasse());
        caracteristiques.setDpeLettre(caracteristiquesDTO.getDpeLettre());
        caracteristiques.setDpeFichier(caracteristiquesDTO.getDpeFichier());
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
    public CaracteristiquesDTO modifierCaracteristiquesPourLogement(String logementMasqueId, CaracteristiquesDTO caracteristiquesModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Caracteristiques caracteristiques = logement.getCaracteristiques();
        if (caracteristiques == null) {
            throw new IllegalArgumentException("Aucunes caracteristiques à modifier pour le logement.");
        }
        if (caracteristiquesModifieeDTO.getDateAchat() == null) {
            throw new IllegalArgumentException("La date d'achat est obligatoire.");
        }
        if (caracteristiquesModifieeDTO.getMontantAchat() == null || caracteristiquesModifieeDTO.getMontantAchat() <= 0) {
            throw new IllegalArgumentException("Le montant d'achat est obligatoire et doit être supérieur à zéro.");
        }
        if (caracteristiquesModifieeDTO.getMontantEstimation() != null && caracteristiquesModifieeDTO.getMontantEstimation() <= 0) {
            throw new IllegalArgumentException("Le montant d'estimation, s'il est présent, doit être supérieur à zéro.");
        }
        if (caracteristiquesModifieeDTO.getMontantFraisDeNotaireEtNegociation() != null && caracteristiquesModifieeDTO.getMontantFraisDeNotaireEtNegociation() <= 0) {
            throw new IllegalArgumentException("Le montant des frais de notaire et négociations, s'il est présent, doit être supérieur à zéro.");
        }
        if (caracteristiquesModifieeDTO.getNombreDePieces() == null || caracteristiquesModifieeDTO.getNombreDePieces() <= 0) {
            throw new IllegalArgumentException("Le nombre de pièces est obligatoire et doit être supérieur à zéro.");
        }
        if (caracteristiquesModifieeDTO.getSurfaceLogement() == null || caracteristiquesModifieeDTO.getSurfaceLogement() <= 0) {
            throw new IllegalArgumentException("La surface du logement est obligatoire et doit être supérieure à zéro.");
        }
        if (caracteristiquesModifieeDTO.getTypeDeLogement() == null) {
            throw new IllegalArgumentException("Le type de logement est obligatoire.");
        }
        if (caracteristiquesModifieeDTO.getMeubleeOuNon() == null) {
            throw new IllegalArgumentException("Veuillez spécifier si le logement est meublé ou non.");
        }
        if (caracteristiquesModifieeDTO.getBalconOuTerrasse() != null && caracteristiquesModifieeDTO.getBalconOuTerrasse() && (caracteristiquesModifieeDTO.getSurfaceBalconOuTerrasse() == null || caracteristiquesModifieeDTO.getSurfaceBalconOuTerrasse() <= 0)) {
            throw new IllegalArgumentException("Si un balcon ou une terrasse est présent(e), la surface doit être supérieure à zéro.");
        }
        if (caracteristiquesModifieeDTO.getDpeLettre() == null) {
            throw new IllegalArgumentException("La lettre DPE est obligatoire.");
        }
        caracteristiques.setDateAchat(caracteristiquesModifieeDTO.getDateAchat());
        caracteristiques.setMontantAchat(caracteristiquesModifieeDTO.getMontantAchat());
        caracteristiques.setMontantEstimation(caracteristiquesModifieeDTO.getMontantEstimation());
        caracteristiques.setMontantFraisDeNotaireEtNegociation(caracteristiquesModifieeDTO.getMontantFraisDeNotaireEtNegociation());
        caracteristiques.setNombreDePieces(caracteristiquesModifieeDTO.getNombreDePieces());
        caracteristiques.setSurfaceLogement(caracteristiquesModifieeDTO.getSurfaceLogement());
        caracteristiques.setTypeDeLogement(caracteristiquesModifieeDTO.getTypeDeLogement());
        caracteristiques.setMeubleeOuNon(caracteristiquesModifieeDTO.getMeubleeOuNon());
        caracteristiques.setBalconOuTerrasse(caracteristiquesModifieeDTO.getBalconOuTerrasse());
        caracteristiques.setSurfaceBalconOuTerrasse(caracteristiquesModifieeDTO.getSurfaceBalconOuTerrasse());
        caracteristiques.setDpeLettre(caracteristiquesModifieeDTO.getDpeLettre());
        caracteristiques.setDpeFichier(caracteristiquesModifieeDTO.getDpeFichier());

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
