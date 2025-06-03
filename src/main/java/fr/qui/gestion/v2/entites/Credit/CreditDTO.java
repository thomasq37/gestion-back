package fr.qui.gestion.v2.entites.Credit;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.enumeration.DpeLettre.DpeLettre;
import fr.qui.gestion.v2.enumeration.TypeDeLogement.TypeDeLogement;
import fr.qui.gestion.v2.enumeration.TypeDeResidence.TypeDeResidence;
import fr.qui.gestion.v2.enumeration.TypeDeTaux.TypeDeTaux;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreditDTO {
    private String masqueId;
    private Double montantEmprunte;
    private Double tauxAnnuelEffectifGlobal;
    private Integer dureeMois;
    private Double mensualite; // Calculé
    private Double coutTotal; // Calculé
    private TypeDeTaux typeDeTaux;
    private Integer jourDePaiementEcheance;
    private LocalDate dateDebut;
}
