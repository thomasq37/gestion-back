package fr.qui.gestion.v2.entites.Locataire;

import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Locataire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    @ManyToOne
    private PeriodeDeLocation periodeDeLocation;
}
