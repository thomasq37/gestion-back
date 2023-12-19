package fr.qui.gestion.user;

import fr.qui.gestion.user.appuser.AppUser;
import lombok.Data;

@Data
public class UserRequest {
    private AppUser user;
    private String token;
}