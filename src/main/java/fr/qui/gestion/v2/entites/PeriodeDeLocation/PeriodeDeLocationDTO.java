package fr.qui.gestion.v2.entites.PeriodeDeLocation;

import fr.qui.gestion.v2.entites.Frais.FraisDTO;
import fr.qui.gestion.v2.entites.Locataire.LocataireDTO;
import fr.qui.gestion.v2.enumeration.TypeDeLocation.TypeDeLocation;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PeriodeDeLocationDTO {
    private String masqueId;
    private Double tarif;
    private LocalDate dateDeDebut;
    private LocalDate dateDeFin;
    private TypeDeLocation typeDeLocation;
    private List<FraisDTO> frais;
    private List<LocataireDTO> locataires;
}
