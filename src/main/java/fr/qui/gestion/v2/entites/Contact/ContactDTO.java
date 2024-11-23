package fr.qui.gestion.v2.entites.Contact;

import lombok.Data;

@Data
public class ContactDTO {
    private String masqueId;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
}