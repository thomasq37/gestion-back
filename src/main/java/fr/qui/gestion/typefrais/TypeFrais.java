package fr.qui.gestion.typefrais;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.frais.Frais;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class TypeFrais {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String nom;
    @JsonIgnore
    @OneToMany(mappedBy = "typeFrais")
    private List<Frais> frais;

}
