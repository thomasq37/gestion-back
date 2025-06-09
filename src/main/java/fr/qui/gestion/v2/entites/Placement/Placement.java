package fr.qui.gestion.v2.entites.Placement;

import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Placement extends AbstractEntityWithMasqueId {
    private String nom;
    private String capital;

    @ManyToOne
    private Utilisateur utilisateur;
}
