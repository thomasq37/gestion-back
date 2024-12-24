package fr.qui.gestion.v2.entites.Alerte;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Alerte extends AbstractEntityWithMasqueId {
    private String probleme;
    private String solution;
    @ManyToOne
    private Logement logement;
}