package fr.qui.gestion.appart.dto;
import lombok.Data;

@Data
public class AppartementCCDTO {
    private Long appartementId;
    private Double prixAchat;
    private Double estimation;
    private Double fraisNotaireEtNegociation;
    private Double revenusNets;
    private Double depensesNettes;
    private Double rentabiliteNette;
    private Double tauxVacanceLocative;
    private Double moyenneBeneficesNetParMois;
    private Double totalTravaux;
    private Double totalFraisGestion;
    private Double totalHonorairesDeLoc;
    private Double totalChargesFixesHorsFrais;

    public AppartementCCDTO(
            Long appartementId,
            Double prixAchat,
            Double estimation,
            Double fraisNotaireEtNegociation,
            Double revenusNets,
            Double depensesNettes,
            Double rentabiliteNette,
            Double tauxVacanceLocative,
            Double moyenneBeneficesNetParMois,
            Double totalTravaux,
            Double totalFraisGestion,
            Double totalHonorairesDeLoc,
            Double totalChargesFixesHorsFrais) {
        this.appartementId = appartementId;
        this.prixAchat = prixAchat;
        this.estimation = estimation;
        this.fraisNotaireEtNegociation = fraisNotaireEtNegociation;
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
