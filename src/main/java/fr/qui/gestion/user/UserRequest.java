package fr.qui.gestion.user;

import lombok.Data;

@Data
public class UserRequest {
    private User user;
    private String token;
}