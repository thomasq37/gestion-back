package fr.qui.gestion.v2.entites.Credit;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.enumeration.TypeDeTaux.TypeDeTaux;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Credit extends AbstractEntityWithMasqueId {
    private Double montantEmprunte;
    private Double tauxAnnuelEffectifGlobal;
    private Integer dureeMois;
    private Double mensualite; // Calculé
    private Double coutTotal; // Calculé
    private TypeDeTaux typeDeTaux;
    private Integer jourDePaiementEcheance;
    private LocalDate dateDebut;
    @OneToOne(mappedBy = "credit")
    private Logement logement;
}