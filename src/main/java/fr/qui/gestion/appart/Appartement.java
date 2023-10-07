package fr.qui.gestion.appart;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import fr.qui.gestion.contact.Contact;
import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.user.AppUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Appartement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numero;
    private String adresse;
    private String codePostal;
    private String ville;
    private int nombrePieces;
    private double surface;
    private boolean balcon;
    private double prix;
    
    @Transient
    private double rentabiliteNette;

    @Transient
    private double tauxVacanceLocative;

    @Transient
    private double moyenneBeneficesNetParMois;

    @PostLoad
    public void calculerMetrics() {
        this.rentabiliteNette = this.calculerRentabiliteNette();
        this.tauxVacanceLocative = this.calculerTauxVacanceLocative();
        this.moyenneBeneficesNetParMois = this.calculerMoyenneBeneficesNetParMois();
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
    
    

    protected double calculerRentabiliteNette() {
        double revenus = 0.0;
        double depensesTotales = 0.0;

        long joursTotal = 0;
        for (PeriodLocation pLocation : this.getPeriodLocation()) {
            long joursLoues = ChronoUnit.DAYS.between(pLocation.getEstEntree(), pLocation.getEstSortie() != null ? pLocation.getEstSortie() : LocalDate.now());
            joursTotal += joursLoues;

            // Calcul des revenus
            if (pLocation.isLocVac()) {
                revenus += joursLoues * pLocation.getPrix();
            } else {
                revenus += joursLoues * (pLocation.getPrix() / 30.0); 
            }

            // Calcul des dépenses pour les frais de chaque période de location
            for (Frais fraisLocation : pLocation.getFrais()) {
                double montantAnnuelEquivalent = fraisLocation.getFrequence().convertirMontantAnnuel(fraisLocation.getMontant());
                depensesTotales += montantAnnuelEquivalent;
            }
        }

        // Nombre d'années de la période de location
        double annees = joursTotal / 365.0;  // Divisé par 365 pour obtenir une approximation du nombre d'années

        // Calcul des dépenses fixes annuelles de l'appartement et multiplication par le nombre d'années
        for (Frais fraisFixeAppart : this.getFraisFixe()) {
            double montantAnnuelEquivalent = fraisFixeAppart.getFrequence().convertirMontantAnnuel(fraisFixeAppart.getMontant());
            depensesTotales += montantAnnuelEquivalent * annees;
        }
        double rNette = revenus - depensesTotales;
        rNette = Math.round(rNette * 100.0) / 100.0;
        // Rentabilité Nette = Revenus - Dépenses
        return rNette;
    }

    protected double calculerMoyenneBeneficesNetParMois() {
        // Obtention de la rentabilité nette (supposant que vous avez une méthode calculerRentabiliteNette)
        double rentabiliteNette = this.calculerRentabiliteNette();

        // Calcul du nombre total de mois sur la période de location
        long joursTotal = 0;
        for (PeriodLocation pLocation : this.getPeriodLocation()) {
            long joursLoues = ChronoUnit.DAYS.between(pLocation.getEstEntree(), pLocation.getEstSortie() != null ? pLocation.getEstSortie() : LocalDate.now());
            joursTotal += joursLoues;
        }
        // Pour simplifier, nous supposons que chaque mois ait 30 jours
        double moisTotal = joursTotal / 30.0;

        // Calcul de la moyenne des bénéfices nets par mois
        double moyenneBeneficesNetParMois = rentabiliteNette / moisTotal;
        moyenneBeneficesNetParMois = Math.round(moyenneBeneficesNetParMois * 100.0) / 100.0;
        return moyenneBeneficesNetParMois;
    }

    protected double calculerTauxVacanceLocative() {
        List<PeriodLocation> periodLocations = this.getPeriodLocation();
        if (periodLocations == null || periodLocations.isEmpty()) {
            return 100.0;  // 100% de vacance si aucune période de location n'est définie
        }
        
        // Trier les périodes de location par date d'entrée
        periodLocations.sort(Comparator.comparing(PeriodLocation::getEstEntree));

        LocalDate premiereEntree = periodLocations.get(0).getEstEntree();
        LocalDate dateActuelle = LocalDate.now();
        long joursDepuisPremiereEntree = ChronoUnit.DAYS.between(premiereEntree, dateActuelle);
        
        long joursOccupes = 0;
        for (PeriodLocation period : periodLocations) {
            joursOccupes += ChronoUnit.DAYS.between(period.getEstEntree(), period.getEstSortie() != null ? period.getEstSortie() : dateActuelle);
        }
        
        long joursVacances = joursDepuisPremiereEntree - joursOccupes;
        double tauxVacance = ((double) joursVacances / joursDepuisPremiereEntree) * 100;

        return Math.round(tauxVacance * 100.0) / 100.0;  // Rond au centième le plus proche
    }


}