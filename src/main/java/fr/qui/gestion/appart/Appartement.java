package fr.qui.gestion.appart;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.mouvementappart.MouvementAppartement;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Appartement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numero;
    private String adresse;
    private String codePostal;
    private String ville;
    private int nombrePieces;
    private double surface;
    private boolean balcon;
    private boolean loue;
    private double loyerMensuel;
    private double prix;
    
    @JsonIgnore
    @OneToMany(mappedBy = "appartement")
    private List<Frais> frais;
    
    @ElementCollection
    @CollectionTable(name = "appartement_images", joinColumns = @JoinColumn(name = "appartement_id"))
    private List<String> images;
    
    @OneToMany(mappedBy = "appartement")
    private List<MouvementAppartement> mouvements;

    
	protected double calculerRentabiliteNette() {
        double totalLoyer = loyerMensuel * 12;

        double totalFrais = 500.0;
        for (Frais frais : frais) {
            switch (frais.getFrequence()) {
                case MENSUELLE:
                    totalFrais += frais.getMontant() * 12;
                    break;
                case TRIMESTRIELLE:
                    totalFrais += frais.getMontant() * 4;
                    break;
                case ANNUELLE:
                    totalFrais += frais.getMontant();
                    break;
            }
        }

        return totalLoyer - totalFrais;
    }
}