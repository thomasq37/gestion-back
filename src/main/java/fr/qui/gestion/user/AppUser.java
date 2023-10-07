package fr.qui.gestion.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.appart.Appartement;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @Column(nullable = true)
    private String email;
    @Column(nullable = true)
    private String phoneNumber;
    
    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    private List<Appartement> appartements;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

}
