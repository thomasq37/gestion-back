package fr.qui.gestion.appart.dto;

import fr.qui.gestion.pays.Pays;
import lombok.Data;

@Data
public class AdresseDTO {
	private Long id;
    private int numero;
    private String adresse;
    private String codePostal;
    private String ville;
    private Pays pays;

    public AdresseDTO(Long id, int numero, String adresse, String codePostal, String ville, Pays pays) {
    	this.id = id;
        this.numero = numero;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
        this.pays = pays;
    }
}
