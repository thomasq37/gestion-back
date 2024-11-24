package fr.qui.gestion.v2.auth;

import lombok.Data;

@Data
public class AuthenticateUserRequestDTO {
    private String email;
    private String mdp;
}
