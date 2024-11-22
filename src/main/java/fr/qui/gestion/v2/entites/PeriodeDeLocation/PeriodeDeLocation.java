package fr.qui.gestion.v2.entites.PeriodeDeLocation;

import fr.qui.gestion.v2.entites.Frais.Frais;
import fr.qui.gestion.v2.entites.Locataire.Locataire;
import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.enumeration.TypeDeLocation.TypeDeLocation;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class PeriodeDeLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double tarif;
    private LocalDate dateDeDebut;
    private LocalDate dateDeFin;
    @Enumerated(EnumType.STRING)
    private TypeDeLocation typeDeLocation;
    @ManyToOne
    private Logement logement;
    @OneToMany(mappedBy = "periodeDeLocation")
    private List<Frais> frais;
    @OneToMany(mappedBy = "periodeDeLocation")
    private List<Locataire> locataires;
}
