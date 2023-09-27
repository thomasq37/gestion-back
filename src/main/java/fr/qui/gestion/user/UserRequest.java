package fr.qui.gestion.user;

import lombok.Data;

@Data
public class UserRequest {
    private AppUser user;
    private String token;
}