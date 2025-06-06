package fr.qui.gestion.v2.entites.Photo;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Photo  extends AbstractEntityWithMasqueId {
    @Column(columnDefinition = "LONGTEXT")
    private String image;
    private Boolean isPrincipal;
    @ManyToOne
    private Logement logement;
}