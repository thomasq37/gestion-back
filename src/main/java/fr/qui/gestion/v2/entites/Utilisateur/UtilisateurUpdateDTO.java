package fr.qui.gestion.v2.entites.Utilisateur;

import lombok.Data;

@Data
public class UtilisateurUpdateDTO {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String mdp;
}
