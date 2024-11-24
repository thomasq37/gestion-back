package fr.qui.gestion.v2.entites.Frais;

import fr.qui.gestion.v2.enumeration.CategorieFrais.CategorieFrais;
import fr.qui.gestion.v2.enumeration.Frequence.Frequence;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FraisDTO {
    private String masqueId;
    private String nom;
    private double montant;
    private LocalDate dateDeDebut;
    private LocalDate dateDeFin;
    private Frequence frequence;
    private CategorieFrais categorieFrais;
}
