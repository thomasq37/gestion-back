package fr.qui.gestion.v2.entites.Contact;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Contact extends AbstractEntityWithMasqueId {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    @ManyToOne
    private Logement logement;
}