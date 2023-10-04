package fr.qui.gestion.contact;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.appart.Appartement;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Contact {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pseudo;
    private String email;
    private String phoneNumber;
    
    @ManyToOne
    @JoinColumn(name = "appartement_id", nullable = true)
    @JsonIgnore
    private Appartement appartement;
}
