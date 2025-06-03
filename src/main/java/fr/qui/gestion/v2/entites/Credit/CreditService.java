package fr.qui.gestion.v2.entites.Credit;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.Logement.LogementRepository;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditService {

    private final CreditRepository creditRepository;
    private final LogementRepository logementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CreditMapper creditMapper;

    public CreditService(
            CreditRepository creditRepository,
            LogementRepository logementRepository,
            UtilisateurRepository utilisateurRepository,
            CreditMapper creditMapper) {
        this.creditRepository = creditRepository;
        this.logementRepository = logementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.creditMapper = creditMapper;
    }

    @Transactional
    public CreditDTO creerCreditPourLogement(String logementMasqueId, CreditDTO creditDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        if (logement.getCredit() != null) {
            throw new IllegalStateException("Un credit existe déjà pour ce logement. Veuillez le modifier.");
        }
        if (creditDTO.getMontantEmprunte() == null || creditDTO.getMontantEmprunte() <= 0) {
            throw new IllegalArgumentException("Le montant emprunté est obligatoire et doit être supérieur à zéro.");
        }
        if (creditDTO.getTauxAnnuelEffectifGlobal() == null || creditDTO.getTauxAnnuelEffectifGlobal() <= 0) {
            throw new IllegalArgumentException("Le taux annuel effectif global est obligatoire et doit être supérieur à zéro.");
        }
        if (creditDTO.getDureeMois() == null || creditDTO.getDureeMois() <= 0) {
            throw new IllegalArgumentException("La durée en mois est obligatoire et doit être supérieur à zéro.");
        }
        if (creditDTO.getTypeDeTaux() == null) {
            throw new IllegalArgumentException("Le type de taux est obligatoire.");
        }
        if (creditDTO.getJourDePaiementEcheance() == null || creditDTO.getJourDePaiementEcheance() <= 0 || creditDTO.getJourDePaiementEcheance() > 28) {
            throw new IllegalArgumentException("Le jour de paiement est obligatoire et doit être compris entre 1 et 28.");
        }
        if (creditDTO.getDateDebut() == null) {
            throw new IllegalArgumentException("La date de début du crédit est obligatoire.");
        }
        Credit credit = new Credit();
        credit.setMontantEmprunte(creditDTO.getMontantEmprunte());
        credit.setTauxAnnuelEffectifGlobal(creditDTO.getTauxAnnuelEffectifGlobal());
        credit.setDureeMois(creditDTO.getDureeMois());
        credit.setTypeDeTaux(creditDTO.getTypeDeTaux());
        credit.setJourDePaiementEcheance(creditDTO.getJourDePaiementEcheance());
        credit.setDateDebut(creditDTO.getDateDebut());
        credit.setMensualite(calculerMensualite(credit));
        credit.setCoutTotal(calculerCoutTotal(credit));
        credit.setLogement(logement);
        Credit savedCredit = creditRepository.save(credit);
        logement.setCredit(savedCredit);
        logementRepository.save(logement);
        return creditMapper.toDto(savedCredit);
    }

    @Transactional(readOnly = true)
    public CreditDTO obtenirCreditPourLogement(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Credit credit = logement.getCredit();
        if (credit == null) {
            throw new IllegalArgumentException("Aucunes credit trouvées pour le logement.");
        }
        return creditMapper.toDto(credit);
    }

    @Transactional
    public CreditDTO modifierCreditPourLogement(String logementMasqueId, CreditDTO creditModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Credit credit = logement.getCredit();
        if (credit == null) {
            throw new IllegalArgumentException("Aucun credit à modifier pour le logement.");
        }
        if (logement.getCredit() != null) {
            throw new IllegalStateException("Un credit existe déjà pour ce logement. Veuillez le modifier.");
        }
        if (creditModifieeDTO.getMontantEmprunte() == null || creditModifieeDTO.getMontantEmprunte() <= 0) {
            throw new IllegalArgumentException("Le montant emprunté est obligatoire et doit être supérieur à zéro.");
        }
        if (creditModifieeDTO.getTauxAnnuelEffectifGlobal() == null || creditModifieeDTO.getTauxAnnuelEffectifGlobal() <= 0) {
            throw new IllegalArgumentException("Le taux annuel effectif global est obligatoire et doit être supérieur à zéro.");
        }
        if (creditModifieeDTO.getDureeMois() == null || creditModifieeDTO.getDureeMois() <= 0) {
            throw new IllegalArgumentException("La durée en mois est obligatoire et doit être supérieur à zéro.");
        }
        if (creditModifieeDTO.getTypeDeTaux() == null) {
            throw new IllegalArgumentException("Le type de taux est obligatoire.");
        }
        if (creditModifieeDTO.getJourDePaiementEcheance() == null || creditModifieeDTO.getJourDePaiementEcheance() <= 0 || creditModifieeDTO.getJourDePaiementEcheance() > 28) {
            throw new IllegalArgumentException("Le jour de paiement est obligatoire et doit être compris entre 1 et 28.");
        }
        if (creditModifieeDTO.getDateDebut() == null) {
            throw new IllegalArgumentException("La date de début du crédit est obligatoire.");
        }
        credit.setMontantEmprunte(creditModifieeDTO.getMontantEmprunte());
        credit.setTauxAnnuelEffectifGlobal(creditModifieeDTO.getTauxAnnuelEffectifGlobal());
        credit.setDureeMois(creditModifieeDTO.getDureeMois());
        credit.setTypeDeTaux(creditModifieeDTO.getTypeDeTaux());
        credit.setJourDePaiementEcheance(creditModifieeDTO.getJourDePaiementEcheance());
        credit.setDateDebut(creditModifieeDTO.getDateDebut());
        credit.setMensualite(calculerMensualite(credit));
        credit.setCoutTotal(calculerCoutTotal(credit));
        Credit savedCredit = creditRepository.save(credit);
        return creditMapper.toDto(savedCredit);
    }



    @Transactional
    public SuccessResponse supprimerCreditPourLogement(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Credit credit = logement.getCredit();
        if (credit == null) {
            throw new IllegalArgumentException("Aucunes credit à supprimer pour le logement.");
        }

        logement.setCredit(null);
        logementRepository.save(logement);
        creditRepository.delete(credit);
        return new SuccessResponse("Les credit ont été supprimé avec succès.");

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
    private Double calculerCoutTotal(Credit credit) {
        return null;
    }

    private Double calculerMensualite(Credit credit) {
        return null;
    }
}
