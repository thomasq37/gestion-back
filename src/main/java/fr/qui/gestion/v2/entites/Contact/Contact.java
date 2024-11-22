package fr.qui.gestion.v2.entites.Contact;

import fr.qui.gestion.v2.entites.Logement.Logement;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    @ManyToOne
    private Logement logement;
}