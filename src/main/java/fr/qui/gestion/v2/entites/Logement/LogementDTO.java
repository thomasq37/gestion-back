package fr.qui.gestion.v2.entites.Logement;

import fr.qui.gestion.v2.entites.Adresse.AdresseDTO;
import fr.qui.gestion.v2.entites.Caracteristiques.CaracteristiquesDTO;
import fr.qui.gestion.v2.entites.Contact.Contact;
import fr.qui.gestion.v2.entites.Frais.Frais;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import fr.qui.gestion.v2.entites.Photo.Photo;
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
    private List<Contact> contacts;
    private List<Frais> frais;
    private List<PeriodeDeLocation> periodesDeLocation;
    private List<Photo> photos;
    private List<Utilisateur> gestionnaires;

}
