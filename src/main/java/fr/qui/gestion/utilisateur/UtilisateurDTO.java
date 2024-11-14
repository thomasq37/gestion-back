package fr.qui.gestion.utilisateur;
import lombok.Data;

@Data
public class UtilisateurDTO {
    private Long id;
    private String email;
    private String pseudo;
    private String phoneNumber;

}
