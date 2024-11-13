package fr.qui.gestion.appart.dto;
import lombok.Data;

@Data
public class AppartementCCDTO {
    private Long appartementId;
    private double prixAchat;
    private double estimation;
    private double revenusNets;
    private double depensesNettes;
    private double rentabiliteNette;
    private double tauxVacanceLocative;
    private double moyenneBeneficesNetParMois;
    private double totalTravaux;
    private double totalFraisGestion;
    private double totalHonorairesDeLoc;
    private double totalChargesFixesHorsFrais;

    public AppartementCCDTO(Long appartementId, double prixAchat,  double estimation, double revenusNets, double depensesNettes, double rentabiliteNette, double tauxVacanceLocative, double moyenneBeneficesNetParMois, double totalTravaux, double totalFraisGestion, double totalHonorairesDeLoc, double totalChargesFixesHorsFrais) {
        this.appartementId = appartementId;
        this.prixAchat = prixAchat;
        this.estimation = estimation;
        this.revenusNets = revenusNets;
        this.depensesNettes = depensesNettes;
        this.rentabiliteNette = rentabiliteNette;
        this.tauxVacanceLocative = tauxVacanceLocative;
        this.moyenneBeneficesNetParMois = moyenneBeneficesNetParMois;
        this.totalTravaux = totalTravaux;
        this.totalFraisGestion = totalFraisGestion;
        this.totalHonorairesDeLoc = totalHonorairesDeLoc;
        this.totalChargesFixesHorsFrais = totalChargesFixesHorsFrais;
    }
}
