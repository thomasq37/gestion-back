package fr.qui.gestion.appart;

import java.util.List;

import fr.qui.gestion.frais.Frais;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @OneToMany(mappedBy = "appartement")
    private List<Frais> frais;

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