package fr.qui.gestion.v2.entites.TotalCompte;

import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class TotalCompte extends AbstractEntityWithMasqueId {
    private Double montant;
    private LocalDate dateEnregistrement;

    @ManyToOne
    private Utilisateur utilisateur;
}
