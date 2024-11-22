package fr.qui.gestion.v2.entites.Frais;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import fr.qui.gestion.v2.enumeration.CategorieFrais.CategorieFrais;
import fr.qui.gestion.v2.enumeration.Frequence.Frequence;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
public class Frais {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private double montant;
    private LocalDate dateDeDebut;
    private LocalDate dateDeFin;
    @Enumerated(EnumType.STRING)
    private Frequence frequence;
    @Enumerated(EnumType.STRING)
    private CategorieFrais categorieFrais;
    @ManyToOne
    private Logement logement;
    @ManyToOne
    private PeriodeDeLocation periodeDeLocation;
}
