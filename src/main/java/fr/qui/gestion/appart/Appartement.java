
package fr.qui.gestion.appart;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import fr.qui.gestion.contact.Contact;
import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.frais.Frequence;
import fr.qui.gestion.pays.Pays;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.user.appuser.AppUser;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Appartement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateAchat;
    private int numero;
    private String adresse;
    private String codePostal;
    private String ville;
    private int nombrePieces;
    private double surface;
    private boolean balcon;
    private double prix;
    private Double fraisNotaireEtNegociation;
    private Double estimation;
    private String dpe;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pays_id")
    private Pays pays;

    @Transient
    private double revenusNets;

    @Transient
    private double depensesNettes;

    @Transient
    private double rentabiliteNette;

    @Transient
    private double tauxVacanceLocative;

    @Transient
    private double moyenneBeneficesNetParMois;

    @Transient
    private double totalFraisGestion;
    @Transient
    private double totalHonorairesDeLoc;

    @PostLoad
    public void calculerMetrics() {
        this.revenusNets = this.calculerRevenusNets();
        this.depensesNettes = this.calculerDepensesNettes();
        this.rentabiliteNette = Math.round((this.revenusNets - this.depensesNettes) * 100.0) / 100.0;
        this.tauxVacanceLocative = this.calculerTauxVacanceLocative();
        this.moyenneBeneficesNetParMois = this.calculerMoyenneBeneficesNetParMois();
        TotauxFrais totaux = this.calculerTotalFrais();
        this.totalFraisGestion = totaux.totalFraisGestion;
        this.totalHonorairesDeLoc = totaux.totalHonorairesDeLoc;
    }

    @OneToMany(mappedBy = "appartement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Frais> fraisFixe;

    @OneToMany(mappedBy = "appartement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Contact> contacts;

    @ElementCollection
    @CollectionTable(name = "appartement_images", joinColumns = @JoinColumn(name = "appartement_id"))
    private List<String> images;

    @OneToMany(mappedBy = "appartement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<PeriodLocation> periodLocation;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToMany
    @JoinTable(
            name = "appartement_gestionnaires",
            joinColumns = @JoinColumn(name = "appartement_id"),
            inverseJoinColumns = @JoinColumn(name = "gestionnaire_id")
    )
    private List<AppUser> gestionnaires;

    public static class TotauxFrais {
        public double totalFraisGestion;
        public double totalHonorairesDeLoc;

        public TotauxFrais(double totalFraisGestion, double totalHonorairesDeLoc) {
            this.totalFraisGestion = totalFraisGestion;
            this.totalHonorairesDeLoc = totalHonorairesDeLoc;
        }
    }
    public TotauxFrais calculerTotalFrais() {
        double totalFraisGestion = 0.0;
        double totalHonorairesDeLoc = 0.0;

        for (PeriodLocation pLocation : this.getPeriodLocation()) {
            Long dureeEnJoursPeriodeLoc = ChronoUnit.DAYS.between(pLocation.getEstEntree(), pLocation.getEstSortie() != null ? pLocation.getEstSortie() : LocalDate.now());

            for (Frais fraisLocation : pLocation.getFrais()) {
                if (fraisLocation.getTypeFrais().getNom().equals("Frais de gestion")) {
                    if (getOccurenceFrequence(fraisLocation.getFrequence()) != 0) {
                        totalFraisGestion += calculerCoutParFrequence(
                                fraisLocation.getMontant(),
                                dureeEnJoursPeriodeLoc,
                                getOccurenceFrequence(fraisLocation.getFrequence())
                        );
                    } else {
                        totalFraisGestion += fraisLocation.getMontant();
                    }
                } else if (fraisLocation.getTypeFrais().getNom().equals("Honor. de loc")) {
                    if (getOccurenceFrequence(fraisLocation.getFrequence()) != 0) {
                        totalHonorairesDeLoc += calculerCoutParFrequence(
                                fraisLocation.getMontant(),
                                dureeEnJoursPeriodeLoc,
                                getOccurenceFrequence(fraisLocation.getFrequence())
                        );
                    } else {
                        totalHonorairesDeLoc += fraisLocation.getMontant();
                    }
                }
            }
        }

        // Retourne un objet contenant les deux totaux
        return new TotauxFrais(
                Math.round(totalFraisGestion * 100.0) / 100.0,
                Math.round(totalHonorairesDeLoc * 100.0) / 100.0
        );
    }
    protected double calculerTotalFraisGestion() {
        double totalFraisGestion = 0.0;
        for (PeriodLocation pLocation : this.getPeriodLocation()) {
            Long dureeEnJoursPeriodeLoc = ChronoUnit.DAYS.between(pLocation.getEstEntree(), pLocation.getEstSortie() != null ? pLocation.getEstSortie() : LocalDate.now());
            for (Frais fraisLocation : pLocation.getFrais()) {
                if (fraisLocation.getTypeFrais().getNom().equals("Frais de gestion")) {
                    if (getOccurenceFrequence(fraisLocation.getFrequence()) != 0) {
                        totalFraisGestion += calculerCoutParFrequence(
                                fraisLocation.getMontant(),
                                dureeEnJoursPeriodeLoc,
                                getOccurenceFrequence(fraisLocation.getFrequence())
                        );
                    } else {
                        totalFraisGestion += fraisLocation.getMontant();
                    }
                }
            }
        }
        return Math.round(totalFraisGestion * 100.0) / 100.0;
    }

    public double calculerCoutParFrequence(double montantParFrequence, Long dureeEnJours, int frequenceEnJours) {
        double nombreDePeriodes = (double) dureeEnJours / frequenceEnJours;
        double resultat = nombreDePeriodes * montantParFrequence;

        // Arrondir à deux chiffres après la virgule
        return Math.round(resultat * 100.0) / 100.0;
    }
    public int getOccurenceFrequence(Frequence frequence) {
        return switch (frequence) {
            case MENSUELLE -> 30; // Environ 30 jours
            case TRIMESTRIELLE -> 90; // Environ 90 jours
            case ANNUELLE -> 365; // Environ 365 jours
            case PONCTUELLE -> 0;
            default -> throw new IllegalArgumentException("Fréquence de frais invalide : " + this);
        };
    }

    protected double calculerDepensesNettes() {
        if(this.getDateAchat() == null){
            return 0.0;
        }
        double depensesTotales = 0.0;
        LocalDate today = LocalDate.now();
        Long dureeEnJours = ChronoUnit.DAYS.between(this.getDateAchat(), today);
        for (Frais fraisFixeAppart : this.getFraisFixe()) {
            if (getOccurenceFrequence(fraisFixeAppart.getFrequence()) != 0) {
                depensesTotales += calculerCoutParFrequence(
                        fraisFixeAppart.getMontant(),
                        dureeEnJours,
                        getOccurenceFrequence(fraisFixeAppart.getFrequence())
                );
            } else {
                depensesTotales += fraisFixeAppart.getMontant();
            }
        }
        for (PeriodLocation pLocation : this.getPeriodLocation()) {
            Long dureeEnJoursPeriodeLoc = ChronoUnit.DAYS.between(pLocation.getEstEntree(), pLocation.getEstSortie() != null ? pLocation.getEstSortie() : LocalDate.now());
            for (Frais fraisLocation : pLocation.getFrais()) {
                if (getOccurenceFrequence(fraisLocation.getFrequence()) != 0) {
                    depensesTotales += calculerCoutParFrequence(
                            fraisLocation.getMontant(),
                            dureeEnJoursPeriodeLoc,
                            getOccurenceFrequence(fraisLocation.getFrequence())
                    );
                } else {
                    depensesTotales += fraisLocation.getMontant();
                }
            }
        }
        return Math.round(depensesTotales * 100.0) / 100.0;
    }

    protected double calculerRevenusNets() {
        double revenus = 0.0;
        for (PeriodLocation pLocation : this.getPeriodLocation()) {
            Long dureeEnJoursPeriodeLoc = ChronoUnit.DAYS.between(pLocation.getEstEntree(), pLocation.getEstSortie() != null ? pLocation.getEstSortie() : LocalDate.now());
            if (pLocation.isLocVac()) {
                revenus += dureeEnJoursPeriodeLoc * pLocation.getPrix();
            } else {
                revenus += dureeEnJoursPeriodeLoc * (pLocation.getPrix() / 30.0);
            }
        }
        return Math.round(revenus * 100.0) / 100.0;
    }

    protected double calculerMoyenneBeneficesNetParMois() {
        if(this.getDateAchat() == null){
            return 0;
        }
        // Obtention de la rentabilité nette (supposant que vous avez une méthode calculerRentabiliteNette)
        double rentabiliteNette = this.revenusNets - this.depensesNettes;
        // Calcul du nombre total de mois sur la période de location
        Long joursTotal = ChronoUnit.DAYS.between(this.getDateAchat(), LocalDate.now());
        double moisTotal = joursTotal / 30.0;
        if (moisTotal == 0) {
            return 0;
        }
        // Calcul de la moyenne des bénéfices nets par mois
        double moyenneBeneficesNetParMois = (rentabiliteNette / moisTotal);
        moyenneBeneficesNetParMois = Math.round(moyenneBeneficesNetParMois * 100.0) / 100.0;
        return moyenneBeneficesNetParMois;
    }

    protected double calculerTauxVacanceLocative() {
        if(this.getDateAchat() == null){
            return 0;
        }
        List<PeriodLocation> periodLocations = this.getPeriodLocation();

        // Si aucune période de location n'est définie, retourner 100% de vacance
        if (periodLocations == null || periodLocations.isEmpty()) {
            return 100.0;  // 100% de vacance si aucune période de location n'est définie
        }

        // Trier les périodes de location par date d'entrée
        periodLocations.sort(Comparator.comparing(PeriodLocation::getEstEntree));

        LocalDate dateActuelle = LocalDate.now();

        // Calculer le nombre de jours depuis la date d'achat
        Long joursDepuisAchat = ChronoUnit.DAYS.between(this.getDateAchat(), dateActuelle);

        Long joursOccupes = 0L;
        for (PeriodLocation period : periodLocations) {
            // Calcul des jours occupés dans la période de location
            LocalDate entree = period.getEstEntree().isBefore(dateAchat) ? dateAchat : period.getEstEntree(); // Utilise dateAchat si la location commence avant l'achat
            joursOccupes += ChronoUnit.DAYS.between(entree,
                    period.getEstSortie() != null ? period.getEstSortie() : dateActuelle);
        }

        // Calcul des jours de vacance depuis l'achat
        Long joursVacances = joursDepuisAchat - joursOccupes;

        // Eviter la division par zéro
        if (joursDepuisAchat <= 0) {
            return 0.0;  // Pas de vacance si le nombre de jours est zéro ou négatif
        }

        // Calculer le taux de vacance locative
        double tauxVacance = ((double) joursVacances / joursDepuisAchat) * 100;

        // Arrondir au centième près
        return Math.round(tauxVacance * 100.0) / 100.0;
    }
}
