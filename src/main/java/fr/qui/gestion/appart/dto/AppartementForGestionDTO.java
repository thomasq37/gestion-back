package fr.qui.gestion.appart.dto;

import java.util.List;

import fr.qui.gestion.pays.Pays;
import fr.qui.gestion.periodlocation.PeriodLocation;
import lombok.Data;

@Data
public class AppartementForGestionDTO {
	private Long id;
    private int numero;
    private String adresse;
    private String codePostal;
    private Pays pays;
    private String ville;
    private int nombrePieces;
    private double surface;
    private boolean balcon;
    private List<String> images;
    private List<PeriodLocation> periodLocation;
    private String appartProprioUsername; 
    private String appartProprioEmail; 
    private String appartProprioPhoneNumber; 
    

}
