package fr.qui.gestion.v2.entites.Locataire;

import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Locataire extends AbstractEntityWithMasqueId {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private LocalDate dateDeNaissance;

    @ManyToOne
    private PeriodeDeLocation periodeDeLocation;
}
