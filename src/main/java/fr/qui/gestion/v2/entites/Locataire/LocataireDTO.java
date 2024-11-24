package fr.qui.gestion.v2.entites.Locataire;
import lombok.Data;

@Data
public class LocataireDTO {
    private String masqueId;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
}
