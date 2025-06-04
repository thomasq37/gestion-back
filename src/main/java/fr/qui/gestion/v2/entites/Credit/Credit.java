package fr.qui.gestion.v2.entites.Credit;

import fr.qui.gestion.v2.entites.Frais.Frais;
import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.enumeration.TypeDeTaux.TypeDeTaux;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

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
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Frais> frais;
}