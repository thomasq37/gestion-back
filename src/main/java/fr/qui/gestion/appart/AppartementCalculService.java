package fr.qui.gestion.appart;

import fr.qui.gestion.appart.dto.AppartementCCDTO;
import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.frais.Frequence;
import fr.qui.gestion.periodlocation.PeriodLocation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class AppartementCalculService {

    public static class TotauxFrais {
        public double totalTravaux;
        public double totalFraisGestion;
        public double totalHonorairesDeLoc;

        public TotauxFrais(double totalTravaux, double totalFraisGestion, double totalHonorairesDeLoc) {
            this.totalTravaux = totalTravaux;
            this.totalFraisGestion = totalFraisGestion;
            this.totalHonorairesDeLoc = totalHonorairesDeLoc;
        }
    }

    public double calculerRevenusNets(Appartement appartement) {
        double revenus = 0.0;
        for (PeriodLocation pLocation : appartement.getPeriodLocation()) {
            Long dureeEnJoursPeriodeLoc = ChronoUnit.DAYS.between(pLocation.getEstEntree(), pLocation.getEstSortie() != null ? pLocation.getEstSortie() : LocalDate.now());
            if (pLocation.isLocVac()) {
                revenus += dureeEnJoursPeriodeLoc * pLocation.getPrix();
            } else {
                revenus += dureeEnJoursPeriodeLoc * (pLocation.getPrix() / 30.0);
            }
        }
        return Math.round(revenus * 100.0) / 100.0;
    }

    protected double calculerMoyenneBeneficesNetParMois(Appartement appartement) {
        if(appartement.getDateAchat() == null){
            return 0;
        }
        TotauxFrais tf = calculerTotalFrais(appartement);
        // Obtention de la rentabilité nette (supposant que vous avez une méthode calculerRentabiliteNette)
        double rentabiliteNette = calculerRevenusNets(appartement) - tf.totalTravaux + tf.totalFraisGestion + tf.totalHonorairesDeLoc + calculerChargesFixesHorsFrais(appartement);
        // Calcul du nombre total de mois sur la période de location
        Long joursTotal = ChronoUnit.DAYS.between(appartement.getDateAchat(), LocalDate.now());
        double moisTotal = joursTotal / 30.0;
        if (moisTotal == 0) {
            return 0;
        }
        // Calcul de la moyenne des bénéfices nets par mois
        double moyenneBeneficesNetParMois = (rentabiliteNette / moisTotal);
        moyenneBeneficesNetParMois = Math.round(moyenneBeneficesNetParMois * 100.0) / 100.0;
        return moyenneBeneficesNetParMois;
    }

    public double calculerChargesFixesHorsFrais(Appartement appartement) {
        if(appartement.getDateAchat() == null){
            return 0.0;
        }
        double chargesFixesHorsFrais = 0.0;
        LocalDate today = LocalDate.now();
        Long dureeEnJours = ChronoUnit.DAYS.between(appartement.getDateAchat(), today);
        for (Frais fraisFixeAppart : appartement.getFraisFixe()) {
            if (getOccurenceFrequence(fraisFixeAppart.getFrequence()) != 0 && !Objects.equals(fraisFixeAppart.getTypeFrais().getNom(), "Frais de gestion")) {
                chargesFixesHorsFrais += calculerCoutParFrequence(
                        fraisFixeAppart.getMontant(),
                        dureeEnJours,
                        getOccurenceFrequence(fraisFixeAppart.getFrequence())
                );
            }
        }
        for (PeriodLocation pLocation : appartement.getPeriodLocation()) {
            Long dureeEnJoursPeriodeLoc = ChronoUnit.DAYS.between(pLocation.getEstEntree(), pLocation.getEstSortie() != null ? pLocation.getEstSortie() : LocalDate.now());
            for (Frais fraisLocation : pLocation.getFrais()) {
                if (getOccurenceFrequence(fraisLocation.getFrequence()) != 0 && !Objects.equals(fraisLocation.getTypeFrais().getNom(), "Frais de gestion")) {
                    chargesFixesHorsFrais += calculerCoutParFrequence(
                            fraisLocation.getMontant(),
                            dureeEnJoursPeriodeLoc,
                            getOccurenceFrequence(fraisLocation.getFrequence())
                    );
                }
            }
        }
        return Math.round(chargesFixesHorsFrais * 100.0) / 100.0;
    }

    public TotauxFrais calculerTotalFrais(Appartement appartement) {
        double totalTravaux = 0.0;
        double totalFraisGestion = 0.0;
        double totalHonorairesDeLoc = 0.0;
        for (Frais fraisFixeAppart : appartement.getFraisFixe()) {
            if (fraisFixeAppart.getTypeFrais().getNom().equals("Travaux")) {
                totalTravaux += fraisFixeAppart.getMontant();
            }
        }
        for (PeriodLocation pLocation : appartement.getPeriodLocation()) {
            Long dureeEnJoursPeriodeLoc = ChronoUnit.DAYS.between(pLocation.getEstEntree(), pLocation.getEstSortie() != null ? pLocation.getEstSortie() : LocalDate.now());

            for (Frais fraisLocation : pLocation.getFrais()) {
                switch (fraisLocation.getTypeFrais().getNom()) {
                    case "Frais de gestion" -> {
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
                    case "Honor. de loc" -> {
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
                    case "Travaux" -> totalTravaux += fraisLocation.getMontant();
                }
            }
        }

        // Retourne un objet contenant les deux totaux
        return new TotauxFrais(
                Math.round(totalTravaux * 100.0) / 100.0,
                Math.round(totalFraisGestion * 100.0) / 100.0,
                Math.round(totalHonorairesDeLoc * 100.0) / 100.0
        );
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

    protected double calculerTauxVacanceLocative(Appartement appartement) {
        if(appartement.getDateAchat() == null){
            return 0;
        }
        List<PeriodLocation> periodLocations = appartement.getPeriodLocation();

        // Si aucune période de location n'est définie, retourner 100% de vacance
        if (periodLocations == null || periodLocations.isEmpty()) {
            return 100.0;  // 100% de vacance si aucune période de location n'est définie
        }

        // Trier les périodes de location par date d'entrée
        periodLocations.sort(Comparator.comparing(PeriodLocation::getEstEntree));

        LocalDate dateActuelle = LocalDate.now();

        // Calculer le nombre de jours depuis la date d'achat
        Long joursDepuisAchat = ChronoUnit.DAYS.between(appartement.getDateAchat(), dateActuelle);

        Long joursOccupes = 0L;
        for (PeriodLocation period : periodLocations) {
            // Calcul des jours occupés dans la période de location
            LocalDate entree = period.getEstEntree().isBefore(appartement.getDateAchat()) ? appartement.getDateAchat() : period.getEstEntree(); // Utilise dateAchat si la location commence avant l'achat
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

    public AppartementCCDTO creerAppartementCCDTO(Appartement appartement) {
        double revenusNets = calculerRevenusNets(appartement);
        double tauxVacanceLocative = calculerTauxVacanceLocative(appartement);
        double moyenneBeneficesNetParMois = calculerMoyenneBeneficesNetParMois(appartement);

        // Calcul des totaux des frais
        TotauxFrais totaux = calculerTotalFrais(appartement);
        double totalTravaux = totaux.totalTravaux;
        double totalFraisGestion = totaux.totalFraisGestion;
        double totalHonorairesDeLoc = totaux.totalHonorairesDeLoc;

        // Calcul des charges fixes
        double totalChargesFixesHorsFrais = calculerChargesFixesHorsFrais(appartement);

        // Calcul des dépenses nettes et de la rentabilité nette
        double depensesNettes = totalTravaux + totalFraisGestion + totalHonorairesDeLoc + totalChargesFixesHorsFrais;
        double rentabiliteNette = Math.round((revenusNets - depensesNettes) * 100.0) / 100.0;

        // Créer et retourner le DTO
        return new AppartementCCDTO(appartement.getId(), appartement.getPrix(), appartement.getEstimation(), revenusNets, depensesNettes, rentabiliteNette, tauxVacanceLocative,
                moyenneBeneficesNetParMois, totalTravaux, totalFraisGestion, totalHonorairesDeLoc, totalChargesFixesHorsFrais);
    }

}

