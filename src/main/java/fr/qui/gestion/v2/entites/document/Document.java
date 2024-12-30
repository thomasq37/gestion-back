package fr.qui.gestion.v2.entites.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Document extends AbstractEntityWithMasqueId {
    private String nom;

    @Column(columnDefinition = "LONGTEXT")
    private String fichier;

    @ManyToMany(mappedBy = "documents", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Logement> logements = new ArrayList<>();
}

