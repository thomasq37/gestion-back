package fr.qui.gestion.v2.entites.Photo;

import fr.qui.gestion.v2.entites.Logement.Logement;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] image;
    @ManyToOne
    private Logement logement;
}