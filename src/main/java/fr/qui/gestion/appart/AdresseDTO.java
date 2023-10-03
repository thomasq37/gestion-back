package fr.qui.gestion.appart;

import lombok.Data;

@Data
public class AdresseDTO {
	private Long id;
    private int numero;
    private String adresse;
    private String codePostal;
    private String ville;

    public AdresseDTO(Long id, int numero, String adresse, String codePostal, String ville) {
    	this.id = id;
        this.numero = numero;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
    }
}
