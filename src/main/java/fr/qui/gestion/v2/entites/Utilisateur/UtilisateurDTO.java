package fr.qui.gestion.v2.entites.Utilisateur;
import lombok.Data;

@Data
public class UtilisateurDTO {
    private String masqueId;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
}
