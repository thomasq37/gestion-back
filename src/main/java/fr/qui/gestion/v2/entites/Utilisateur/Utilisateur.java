package fr.qui.gestion.v2.entites.Utilisateur;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.Role.Role;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Utilisateur extends AbstractEntityWithMasqueId {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String mdp;
    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Logement> logementsProprietaire;
    @ManyToMany(mappedBy = "gestionnaires")
    private List<Logement> logementsGestionnaire;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
