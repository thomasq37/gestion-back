package fr.qui.gestion.v2.entites.Caracteristiques;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.enumeration.DpeLettre.DpeLettre;
import fr.qui.gestion.v2.enumeration.TypeDeLogement.TypeDeLogement;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Caracteristiques extends AbstractEntityWithMasqueId {
    private LocalDate dateAchat;
    private Double montantAchat;
    private Double montantEstimation;
    private Double montantFraisDeNotaireEtNegociation;
    private Integer nombreDePieces;
    private Double surfaceLogement;
    @Enumerated(EnumType.STRING)
    private TypeDeLogement typeDeLogement;
    private Boolean meubleeOuNon;
    private Boolean balconOuTerrasse;
    private Double surfaceBalconOuTerrasse;
    private DpeLettre dpeLettre;
    @Column(columnDefinition = "TEXT")
    private String dpeFichier;
    @OneToOne(mappedBy = "caracteristiques")
    private Logement logement;
}
