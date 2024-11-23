package fr.qui.gestion.v2.entites.Logement;

import fr.qui.gestion.v2.entites.Adresse.AdresseDTO;
import fr.qui.gestion.v2.entites.Caracteristiques.CaracteristiquesDTO;
import fr.qui.gestion.v2.entites.Contact.ContactDTO;
import fr.qui.gestion.v2.entites.Frais.Frais;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import fr.qui.gestion.v2.entites.Photo.PhotoDTO;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurDTO;
import lombok.Data;

import java.util.List;

@Data
public class LogementDTO {

    private String masqueId;
    private UtilisateurDTO proprietaire;
    private AdresseDTO adresse;
    private CaracteristiquesDTO caracteristiques;
    private List<ContactDTO> contacts;
    private List<Frais> frais;
    private List<PeriodeDeLocation> periodesDeLocation;
    private List<PhotoDTO> photos;
    private List<Utilisateur> gestionnaires;

}
