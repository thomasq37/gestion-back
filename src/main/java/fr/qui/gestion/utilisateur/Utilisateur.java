package fr.qui.gestion.utilisateur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.role.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class Utilisateur {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true) 
	 private String email;
	@Column(nullable = true)
	private String phoneNumber;
	@OneToMany(mappedBy = "appUser")
	@JsonIgnore
	private List<Appartement> appartements;
    private String pseudo;
    private String mdp;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
