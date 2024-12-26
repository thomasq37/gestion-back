package fr.qui.gestion.v2.entites.PeriodeDeLocation;

import fr.qui.gestion.v2.entites.Frais.Frais;
import fr.qui.gestion.v2.entites.Locataire.Locataire;
import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.enumeration.TypeDeLocation.TypeDeLocation;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class PeriodeDeLocation extends AbstractEntityWithMasqueId {
    private Double tarif;
    private LocalDate dateDeDebut;
    private LocalDate dateDeFin;

    @Enumerated(EnumType.STRING)
    private TypeDeLocation typeDeLocation;

    @ManyToOne
    private Logement logement;

    @OneToMany(mappedBy = "periodeDeLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Frais> frais;

    @OneToMany(mappedBy = "periodeDeLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Locataire> locataires;
}
