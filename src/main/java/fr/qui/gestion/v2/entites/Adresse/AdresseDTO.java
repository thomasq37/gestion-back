package fr.qui.gestion.v2.entites.Adresse;

import fr.qui.gestion.v2.enumeration.Pays.Pays;
import lombok.Data;

@Data
public class AdresseDTO {
    private String masqueId;
    private String numero;
    private String voie;
    private String complementAdresse;
    private String codePostal;
    private String ville;
    private Pays pays;
}
