package fr.qui.gestion.appart.dto;
import lombok.Data;

@Data
public class ChiffresClesDTO {
	private Integer valeurTotale;
    private Integer estimationTotale;
    private Integer revenusTotaux;
    private Integer depensesTotales;
    public ChiffresClesDTO(Integer valeurTotale, Integer estimationTotale, Integer revenusTotaux, Integer depensesTotales) {
    	this.valeurTotale = valeurTotale;
        this.estimationTotale = estimationTotale;
        this.revenusTotaux = revenusTotaux;
        this.depensesTotales = depensesTotales;
    }
}
