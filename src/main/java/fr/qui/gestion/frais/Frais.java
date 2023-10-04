package fr.qui.gestion.frais;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.typefrais.TypeFrais;
import jakarta.persistence.Column;
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
    private double montant;
    @Enumerated(EnumType.STRING)
    private Frequence frequence;

    @ManyToOne
    @JoinColumn(name = "appartement_id", nullable = true)
    @JsonIgnore
    private Appartement appartement;
  

    @ManyToOne
    @JoinColumn(name = "period_location_id", nullable = true)
    @JsonIgnore
    private PeriodLocation periodLocation;
    
    @ManyToOne
    @JoinColumn(name = "type_frais_id")
    private TypeFrais typeFrais;
    
  
    
}