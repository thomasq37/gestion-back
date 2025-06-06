package fr.qui.gestion.v2.entites.Frais;

import fr.qui.gestion.v2.entites.Credit.Credit;
import fr.qui.gestion.v2.entites.Credit.CreditRepository;
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
import java.util.Objects;

@Service
public class FraisService {

    private final FraisRepository fraisRepository;
    private final LogementRepository logementRepository;
    private final PeriodeDeLocationRepository periodeDeLocationRepository;
    private final CreditRepository creditRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final FraisMapper fraisMapper;

    public FraisService(
            FraisRepository fraisRepository,
            LogementRepository logementRepository,
            PeriodeDeLocationRepository periodeDeLocationRepository,
            CreditRepository creditRepository,
            UtilisateurRepository utilisateurRepository,
            FraisMapper fraisMapper) {
        this.fraisRepository = fraisRepository;
        this.logementRepository = logementRepository;
        this.periodeDeLocationRepository = periodeDeLocationRepository;
        this.creditRepository = creditRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.fraisMapper = fraisMapper;
    }
    // ----------------------------------------------- //
    // Debut des frais concernant logemet              //
    // ----------------------------------------------- //

    public List<FraisDTO> listerFraisPourLogement(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        List<Frais> frais = fraisRepository.findByLogement(logement);
        return frais.stream().map(fraisMapper::toDto).toList();
    }
    @Transactional
    public FraisDTO creerFraisPourLogement(String logementMasqueId, FraisDTO fraisDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        if (fraisDTO.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant du frais doit être supérieur à zéro.");
        }
        if (fraisDTO.getDateDeDebut() == null) {
            throw new IllegalArgumentException("La date de début du frais est obligatoire.");
        }
        if (fraisDTO.getDateDeFin() != null && fraisDTO.getDateDeFin().isBefore(fraisDTO.getDateDeDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }
        if (fraisDTO.getFrequence() == null) {
            throw new IllegalArgumentException("La fréquence du frais est obligatoire.");
        }
        if (fraisDTO.getCategorieFrais() == null) {
            throw new IllegalArgumentException("La catégorie du frais est obligatoire.");
        }
        if (fraisDTO.getDateDeFin() != null && fraisDTO.getDateDeDebut().isEqual(fraisDTO.getDateDeFin())
                && !"PONCTUELLE".equalsIgnoreCase(String.valueOf(fraisDTO.getFrequence()))) {
            throw new IllegalArgumentException("Si la date de début et la date de fin sont identiques, la fréquence doit être 'PONCTUELLE'.");
        }
        Frais frais = new Frais();
        frais.setNom(fraisDTO.getNom());
        frais.setMontant(fraisDTO.getMontant());
        frais.setDateDeDebut(fraisDTO.getDateDeDebut());
        frais.setDateDeFin(fraisDTO.getDateDeFin());
        frais.setFrequence(fraisDTO.getFrequence());
        frais.setCategorieFrais(fraisDTO.getCategorieFrais());
        frais.setLogement(logement);
        Frais savedFrais = fraisRepository.save(frais);
        logement.getFrais().add(savedFrais);
        logementRepository.save(logement);
        return fraisMapper.toDto(savedFrais);
    }
    @Transactional(readOnly = true)
    public FraisDTO obtenirFraisPourLogement(String logementMasqueId, String fraisMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Frais frais = logement.getFrais().stream()
                .filter(c -> c.getMasqueId().equals(fraisMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou frais introuvable."));
        return fraisMapper.toDto(frais);
    }
    @Transactional
    public FraisDTO modifierFraisPourLogement(String logementMasqueId, String fraisMasqueId, FraisDTO fraisModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Frais frais = logement.getFrais().stream()
                .filter(c -> c.getMasqueId().equals(fraisMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou frais introuvable."));
        if (fraisModifieeDTO.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant du frais doit être supérieur à zéro.");
        }
        if (fraisModifieeDTO.getDateDeDebut() == null) {
            throw new IllegalArgumentException("La date de début du frais est obligatoire.");
        }
        if (fraisModifieeDTO.getDateDeFin() != null && fraisModifieeDTO.getDateDeFin().isBefore(fraisModifieeDTO.getDateDeDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }
        if (fraisModifieeDTO.getFrequence() == null) {
            throw new IllegalArgumentException("La fréquence du frais est obligatoire.");
        }
        if (fraisModifieeDTO.getCategorieFrais() == null) {
            throw new IllegalArgumentException("La catégorie du frais est obligatoire.");
        }
        if (fraisModifieeDTO.getDateDeFin() != null && fraisModifieeDTO.getDateDeDebut().isEqual(fraisModifieeDTO.getDateDeFin())
                && !"PONCTUELLE".equalsIgnoreCase(String.valueOf(fraisModifieeDTO.getFrequence()))) {
            throw new IllegalArgumentException("Si la date de début et la date de fin sont identiques, la fréquence doit être 'PONCTUELLE'.");
        }
        frais.setNom(fraisModifieeDTO.getNom());
        frais.setMontant(fraisModifieeDTO.getMontant());
        frais.setDateDeDebut(fraisModifieeDTO.getDateDeDebut());
        frais.setDateDeFin(fraisModifieeDTO.getDateDeFin());
        frais.setFrequence(fraisModifieeDTO.getFrequence());
        frais.setCategorieFrais(fraisModifieeDTO.getCategorieFrais());
        Frais savedFrais = fraisRepository.save(frais);
        return fraisMapper.toDto(savedFrais);
    }
    @Transactional
    public SuccessResponse supprimerFraisPourLogement(String logementMasqueId, String fraisMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Frais frais = logement.getFrais().stream()
                .filter(c -> c.getMasqueId().equals(fraisMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou frais introuvable."));
        logement.getFrais().remove(frais);
        logementRepository.save(logement);
        fraisRepository.delete(frais);
        return new SuccessResponse("Le frais a été supprimé avec succès.");

    }

    // ----------------------------------------------- //
    // Debut des frais concernant periodes de location //
    // ----------------------------------------------- //

    public List<FraisDTO> listerFraisPourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));
        List<Frais> frais = fraisRepository.findByPeriodeDeLocation(periodeDeLocation);
        return frais.stream()
                .map(fraisMapper::toDto)
                .toList();
    }
    @Transactional
    public FraisDTO creerFraisPourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId, FraisDTO fraisDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));

        if (fraisDTO.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant du frais doit être supérieur à zéro.");
        }
        if (fraisDTO.getDateDeDebut() == null) {
            throw new IllegalArgumentException("La date de début du frais est obligatoire.");
        }
        if (fraisDTO.getDateDeFin() != null && fraisDTO.getDateDeFin().isBefore(fraisDTO.getDateDeDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }
        if (fraisDTO.getFrequence() == null) {
            throw new IllegalArgumentException("La fréquence du frais est obligatoire.");
        }
        if (fraisDTO.getCategorieFrais() == null) {
            throw new IllegalArgumentException("La catégorie du frais est obligatoire.");
        }
        if (fraisDTO.getDateDeFin() != null && fraisDTO.getDateDeDebut().isEqual(fraisDTO.getDateDeFin())
                && !"PONCTUELLE".equalsIgnoreCase(String.valueOf(fraisDTO.getFrequence()))) {
            throw new IllegalArgumentException("Si la date de début et la date de fin sont identiques, la fréquence doit être 'PONCTUELLE'.");
        }
        Frais frais = new Frais();
        frais.setNom(fraisDTO.getNom());
        frais.setMontant(fraisDTO.getMontant());
        frais.setDateDeDebut(fraisDTO.getDateDeDebut());
        frais.setDateDeFin(fraisDTO.getDateDeFin());
        frais.setFrequence(fraisDTO.getFrequence());
        frais.setCategorieFrais(fraisDTO.getCategorieFrais());
        frais.setPeriodeDeLocation(periodeDeLocation);
        Frais savedFrais = fraisRepository.save(frais);
        periodeDeLocation.getFrais().add(savedFrais);
        periodeDeLocationRepository.save(periodeDeLocation);
        return fraisMapper.toDto(savedFrais);
    }
    @Transactional(readOnly = true)
    public FraisDTO obtenirFraisPourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId, String fraisMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));

        Frais frais = periodeDeLocation.getFrais().stream()
                .filter(c -> c.getMasqueId().equals(fraisMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou frais introuvable."));

        return fraisMapper.toDto(frais);
    }
    @Transactional
    public FraisDTO modifierFraisPourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId, String fraisMasqueId, FraisDTO fraisModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));

        Frais frais = periodeDeLocation.getFrais().stream()
                .filter(c -> c.getMasqueId().equals(fraisMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou frais introuvable."));

        if (fraisModifieeDTO.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant du frais doit être supérieur à zéro.");
        }
        if (fraisModifieeDTO.getDateDeDebut() == null) {
            throw new IllegalArgumentException("La date de début du frais est obligatoire.");
        }
        if (fraisModifieeDTO.getDateDeFin() != null && fraisModifieeDTO.getDateDeFin().isBefore(fraisModifieeDTO.getDateDeDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }
        if (fraisModifieeDTO.getFrequence() == null) {
            throw new IllegalArgumentException("La fréquence du frais est obligatoire.");
        }
        if (fraisModifieeDTO.getCategorieFrais() == null) {
            throw new IllegalArgumentException("La catégorie du frais est obligatoire.");
        }
        if (fraisModifieeDTO.getDateDeFin() != null && fraisModifieeDTO.getDateDeDebut().isEqual(fraisModifieeDTO.getDateDeFin())
                && !"PONCTUELLE".equalsIgnoreCase(String.valueOf(fraisModifieeDTO.getFrequence()))) {
            throw new IllegalArgumentException("Si la date de début et la date de fin sont identiques, la fréquence doit être 'PONCTUELLE'.");
        }
        frais.setNom(fraisModifieeDTO.getNom());
        frais.setMontant(fraisModifieeDTO.getMontant());
        frais.setDateDeDebut(fraisModifieeDTO.getDateDeDebut());
        frais.setDateDeFin(fraisModifieeDTO.getDateDeFin());
        frais.setFrequence(fraisModifieeDTO.getFrequence());
        frais.setCategorieFrais(fraisModifieeDTO.getCategorieFrais());
        Frais savedFrais = fraisRepository.save(frais);
        return fraisMapper.toDto(savedFrais);
    }
    @Transactional
    public SuccessResponse supprimerFraisPourPeriodeDeLocation(String logementMasqueId, String periodeMasqueId, String fraisMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        PeriodeDeLocation periodeDeLocation = logement.getPeriodesDeLocation().stream()
                .filter(periode -> periode.getMasqueId().equals(periodeMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou période introuvable pour ce logement."));

        Frais frais = periodeDeLocation.getFrais().stream()
                .filter(c -> c.getMasqueId().equals(fraisMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou frais introuvable."));

        periodeDeLocation.getFrais().remove(frais);
        periodeDeLocationRepository.save(periodeDeLocation);
        fraisRepository.delete(frais);
        return new SuccessResponse("Le frais a été supprimé avec succès.");
    }

    // ----------------------------------------------- //
    // Debut des frais concernant credit               //
    // ----------------------------------------------- //

    public List<FraisDTO> listerFraisPourCredit(String logementMasqueId, String creditMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Credit credit = logement.getCredit();
        if(!Objects.equals(creditMasqueId, credit.getMasqueId())){
           throw  new SecurityException("Accès interdit ou credit introuvable pour ce logement.");
        }
        List<Frais> frais = fraisRepository.findByCredit(credit);
        return frais.stream()
                .map(fraisMapper::toDto)
                .toList();
    }
    @Transactional
    public FraisDTO creerFraisPourCredit(String logementMasqueId, String creditMasqueId, FraisDTO fraisDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Credit credit = logement.getCredit();
        if(!Objects.equals(creditMasqueId, credit.getMasqueId())){
            throw  new SecurityException("Accès interdit ou credit introuvable pour ce logement.");
        }
        if (fraisDTO.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant du frais doit être supérieur à zéro.");
        }
        if (fraisDTO.getDateDeDebut() == null) {
            throw new IllegalArgumentException("La date de début du frais est obligatoire.");
        }
        if (fraisDTO.getDateDeFin() != null && fraisDTO.getDateDeFin().isBefore(fraisDTO.getDateDeDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }
        if (fraisDTO.getFrequence() == null) {
            throw new IllegalArgumentException("La fréquence du frais est obligatoire.");
        }
        if (fraisDTO.getCategorieFrais() == null) {
            throw new IllegalArgumentException("La catégorie du frais est obligatoire.");
        }
        if (fraisDTO.getDateDeFin() != null && fraisDTO.getDateDeDebut().isEqual(fraisDTO.getDateDeFin())
                && !"PONCTUELLE".equalsIgnoreCase(String.valueOf(fraisDTO.getFrequence()))) {
            throw new IllegalArgumentException("Si la date de début et la date de fin sont identiques, la fréquence doit être 'PONCTUELLE'.");
        }
        Frais frais = new Frais();
        frais.setNom(fraisDTO.getNom());
        frais.setMontant(fraisDTO.getMontant());
        frais.setDateDeDebut(fraisDTO.getDateDeDebut());
        frais.setDateDeFin(fraisDTO.getDateDeFin());
        frais.setFrequence(fraisDTO.getFrequence());
        frais.setCategorieFrais(fraisDTO.getCategorieFrais());
        frais.setCredit(credit);
        Frais savedFrais = fraisRepository.save(frais);
        credit.getFrais().add(savedFrais);
        creditRepository.save(credit);
        return fraisMapper.toDto(savedFrais);
    }
    @Transactional(readOnly = true)
    public FraisDTO obtenirFraisPourCredit(String logementMasqueId, String creditMasqueId, String fraisMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        Credit credit = logement.getCredit();
        if(!Objects.equals(creditMasqueId, credit.getMasqueId())){
            throw  new SecurityException("Accès interdit ou credit introuvable pour ce logement.");
        }
        Frais frais = credit.getFrais().stream()
                .filter(c -> c.getMasqueId().equals(fraisMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou frais introuvable."));

        return fraisMapper.toDto(frais);
    }
    @Transactional
    public FraisDTO modifierFraisPourCredit(String logementMasqueId, String creditMasqueId, String fraisMasqueId, FraisDTO fraisModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        Credit credit = logement.getCredit();
        if(!Objects.equals(creditMasqueId, credit.getMasqueId())){
            throw  new SecurityException("Accès interdit ou credit introuvable pour ce logement.");
        }
        Frais frais = credit.getFrais().stream()
                .filter(c -> c.getMasqueId().equals(fraisMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou frais introuvable."));

        if (fraisModifieeDTO.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant du frais doit être supérieur à zéro.");
        }
        if (fraisModifieeDTO.getDateDeDebut() == null) {
            throw new IllegalArgumentException("La date de début du frais est obligatoire.");
        }
        if (fraisModifieeDTO.getDateDeFin() != null && fraisModifieeDTO.getDateDeFin().isBefore(fraisModifieeDTO.getDateDeDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }
        if (fraisModifieeDTO.getFrequence() == null) {
            throw new IllegalArgumentException("La fréquence du frais est obligatoire.");
        }
        if (fraisModifieeDTO.getCategorieFrais() == null) {
            throw new IllegalArgumentException("La catégorie du frais est obligatoire.");
        }
        if (fraisModifieeDTO.getDateDeFin() != null && fraisModifieeDTO.getDateDeDebut().isEqual(fraisModifieeDTO.getDateDeFin())
                && !"PONCTUELLE".equalsIgnoreCase(String.valueOf(fraisModifieeDTO.getFrequence()))) {
            throw new IllegalArgumentException("Si la date de début et la date de fin sont identiques, la fréquence doit être 'PONCTUELLE'.");
        }
        frais.setNom(fraisModifieeDTO.getNom());
        frais.setMontant(fraisModifieeDTO.getMontant());
        frais.setDateDeDebut(fraisModifieeDTO.getDateDeDebut());
        frais.setDateDeFin(fraisModifieeDTO.getDateDeFin());
        frais.setFrequence(fraisModifieeDTO.getFrequence());
        frais.setCategorieFrais(fraisModifieeDTO.getCategorieFrais());
        Frais savedFrais = fraisRepository.save(frais);
        return fraisMapper.toDto(savedFrais);
    }
    @Transactional
    public SuccessResponse supprimerFraisPourCredit(String logementMasqueId, String creditMasqueId, String fraisMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        Credit credit = logement.getCredit();
        if(!Objects.equals(creditMasqueId, credit.getMasqueId())){
            throw  new SecurityException("Accès interdit ou credit introuvable pour ce logement.");
        }
        Frais frais = credit.getFrais().stream()
                .filter(c -> c.getMasqueId().equals(fraisMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou frais introuvable."));

        credit.getFrais().remove(frais);
        creditRepository.save(credit);
        fraisRepository.delete(frais);
        return new SuccessResponse("Le frais a été supprimé avec succès.");
    }

    // ----------------------------------------------- //
    // Global                                          //
    // ----------------------------------------------- //

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
