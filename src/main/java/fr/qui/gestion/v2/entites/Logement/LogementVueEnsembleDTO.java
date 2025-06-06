package fr.qui.gestion.v2.entites.Logement;

import fr.qui.gestion.v2.entites.Adresse.AdresseDTO;
import fr.qui.gestion.v2.entites.Alerte.AlerteDTO;
import fr.qui.gestion.v2.entites.Caracteristiques.CaracteristiquesDTO;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocationDTO;
import fr.qui.gestion.v2.entites.Photo.PhotoDTO;
import lombok.Data;

import java.util.List;
@Data
public class LogementVueEnsembleDTO {
    private String masqueId;
    private AdresseDTO adresse;
    private CaracteristiquesDTO caracteristiques;
    private List<AlerteDTO> alertes;
    private List<PeriodeDeLocationDTO> periodesDeLocation;
    private List<PhotoDTO> photos;
}
