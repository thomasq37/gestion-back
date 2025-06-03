package fr.qui.gestion.v2.entites.Logement;

import fr.qui.gestion.v2.entites.Adresse.Adresse;
import fr.qui.gestion.v2.entites.Alerte.Alerte;
import fr.qui.gestion.v2.entites.Caracteristiques.Caracteristiques;
import fr.qui.gestion.v2.entites.Contact.Contact;
import fr.qui.gestion.v2.entites.Credit.Credit;
import fr.qui.gestion.v2.entites.Frais.Frais;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import fr.qui.gestion.v2.entites.Photo.Photo;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.document.Document;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "logement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts;

    @OneToMany(mappedBy = "logement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alerte> alertes;

    @OneToMany(mappedBy = "logement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Frais> frais;

    @OneToMany(mappedBy = "logement")
    private List<PeriodeDeLocation> periodesDeLocation;

    @OneToMany(mappedBy = "logement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "logement_document",
            joinColumns = @JoinColumn(name = "logement_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> documents = new ArrayList<>();
    @ManyToMany
    private List<Utilisateur> gestionnaires;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Credit credit;
}

