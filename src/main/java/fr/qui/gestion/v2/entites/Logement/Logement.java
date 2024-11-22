package fr.qui.gestion.v2.entites.Logement;

import fr.qui.gestion.v2.entites.Adresse.Adresse;
import fr.qui.gestion.v2.entites.Caracteristiques.Caracteristiques;
import fr.qui.gestion.v2.entites.Contact.Contact;
import fr.qui.gestion.v2.entites.Frais.Frais;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import fr.qui.gestion.v2.entites.Photo.Photo;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Logement  extends AbstractEntityWithMasqueId {
    @NotNull(message = "Le propriétaire est obligatoire")
    @ManyToOne
    private Utilisateur proprietaire;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Adresse adresse;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Caracteristiques caracteristiques;
    @OneToMany(mappedBy = "logement")
    private List<Contact> contacts;
    @OneToMany(mappedBy = "logement")
    private List<Frais> frais;
    @OneToMany(mappedBy = "logement")
    private List<PeriodeDeLocation> periodesDeLocation;
    @OneToMany(mappedBy = "logement")
    private List<Photo> photos;
    @ManyToMany
    private List<Utilisateur> gestionnaires;
}
