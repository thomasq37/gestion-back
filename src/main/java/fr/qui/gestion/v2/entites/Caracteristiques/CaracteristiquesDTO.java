package fr.qui.gestion.v2.entites.Caracteristiques;

import fr.qui.gestion.v2.enumeration.DpeLettre.DpeLettre;
import fr.qui.gestion.v2.enumeration.TypeDeLogement.TypeDeLogement;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CaracteristiquesDTO {
    private String masqueId;
    private LocalDate dateAchat;
    private Double montantAchat;
    private Double montantEstimation;
    private Double montantFraisDeNotaireEtNegociation;
    private Integer nombreDePieces;
    private Double surfaceLogement;
    private TypeDeLogement typeDeLogement;
    private Boolean meubleeOuNon;
    private Boolean balconOuTerrasse;
    private Double surfaceBalconOuTerrasse;
    private DpeLettre dpeLettre;
    private byte[] dpeFichier;
}
