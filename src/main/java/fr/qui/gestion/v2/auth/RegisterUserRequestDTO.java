package fr.qui.gestion.v2.auth;

import lombok.Data;

@Data
public class RegisterUserRequestDTO {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String mdp;
}
