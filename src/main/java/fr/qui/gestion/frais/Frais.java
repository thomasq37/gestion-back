package fr.qui.gestion.frais;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.typefrais.TypeFrais;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Frais {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public double montant;
    @Enumerated(EnumType.STRING)
    private Frequence frequence;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "appartement_id")
    private Appartement appartement;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "type_frais_id")
    private TypeFrais typeFrais;
    
}