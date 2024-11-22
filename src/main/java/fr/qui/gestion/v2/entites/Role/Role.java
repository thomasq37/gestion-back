package fr.qui.gestion.v2.entites.Role;

import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    @ManyToMany(mappedBy = "roles")
    private List<Utilisateur> utilisateurs;

}
