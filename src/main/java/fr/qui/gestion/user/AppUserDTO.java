package fr.qui.gestion.user;

import lombok.Data;

@Data
public class AppUserDTO {
	private Long id;
    private String username;
    private String email;
    private String phoneNumber;
}