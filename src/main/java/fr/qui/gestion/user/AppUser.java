package fr.qui.gestion.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.appart.Appartement;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String username;
    private String password;  
    private String userToken;
    
    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    private List<Appartement> appartements;

}
